package com.rutar.flashlight_widget;

import android.util.*;
import android.view.*;
import android.content.*;
import android.graphics.*;

public class Color_Picker extends View implements View.OnTouchListener {

private int alpha = 0;
private int color = 0;

private float[] hsv = new float[3];

private float cw = 0.08f;
private float lw = 0.01f;

private float tmp_value;

private int cMode = 0;

private boolean show_text = true;
private boolean show_result = true;

protected static final int SET_COLOR = 0;
protected static final int SET_SATUR = 1;
protected static final int SET_ALPHA = 2;

private float cx; // Центр по вісі x
private float cy; // Центр по вісі y

private int	size; // Розмір view компонента

private float angle_1 = 0;
private float angle_2 = 0;
private float angle_3 = 0;

private float radius_1 = -1;
private float radius_2 = -1;
private float radius_3 = -1;
private float radius_4 = -1;

private float rotate_angle;

private SweepGradient sg = null;

private Paint p_text     = new Paint(Paint.ANTI_ALIAS_FLAG);
private Paint p_line     = new Paint(Paint.ANTI_ALIAS_FLAG);
private Paint p_circles  = new Paint(Paint.ANTI_ALIAS_FLAG);
private Paint p_circle_1 = new Paint(Paint.ANTI_ALIAS_FLAG);
private Paint p_circle_2 = new Paint(Paint.ANTI_ALIAS_FLAG);
private Paint p_circle_3 = new Paint(Paint.ANTI_ALIAS_FLAG);
private Paint p_circle_4 = new Paint(Paint.ANTI_ALIAS_FLAG);

final int[] picker_colors = new int[] { Color.RED,  Color.YELLOW, Color.GREEN,
                                        Color.CYAN, Color.BLUE,   Color.MAGENTA, Color.RED };

private OnColorChangeListener listener = null;

///////////////////////////////////////////////////////////////////////////////////////////////////

public interface OnColorChangeListener { public void onDismiss(int color);
                                         public void onColorChanged(int color); }

///////////////////////////////////////////////////////////////////////////////////////////////////

public Color_Picker (Context context) { this(context, null);
                                        init(context); }

///////////////////////////////////////////////////////////////////////////////////////////////////

public Color_Picker (Context context, AttributeSet attrs) { this(context, attrs, 0);
                                                            init(context); }

///////////////////////////////////////////////////////////////////////////////////////////////////

public Color_Picker (Context context, AttributeSet attrs, int defStyle) {

super(context, attrs, defStyle);
init(context);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public void setOnColorChangeListener(OnColorChangeListener listener) { this.listener = listener; }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Ініціалізація необхідних перемінних

private void init (Context context) {

setFocusable(true);
setOnTouchListener(this);

p_line.setColor(Color.WHITE);
p_line.setStyle(Paint.Style.STROKE);

p_circles.setColor(Color.WHITE);
p_circles.setStyle(Paint.Style.STROKE);

p_text.setTextAlign(Paint.Align.CENTER);
p_circle_1.setStyle(Paint.Style.STROKE);

p_circle_2.setColor(Color.DKGRAY);
p_circle_2.setStyle(Paint.Style.STROKE);

p_circle_3.setColor(Color.DKGRAY);
p_circle_3.setStyle(Paint.Style.STROKE);

p_circle_4.setColor(Color.RED);
p_circle_4.setStyle(Paint.Style.FILL_AND_STROKE);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод викликається при зміні розмірів дисплею (напр. при зміні орієнтації)

@Override
protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {

int mWidth = measure(widthMeasureSpec);
int mHeight = measure(heightMeasureSpec);
size = Math.min(mWidth, mHeight);

setMeasuredDimension(size, size);
calculate_Sizes();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private void calculate_Sizes() {

// Розрахунок центру view компонента
cx = size * 0.5f;
cy = cx;

radius_1 = size * 0.44f;
radius_2 = size * 0.33f;
radius_3 = size * 0.22f;
radius_4 = size * 0.11f;

p_line.setStrokeWidth(size * lw);
p_circle_1.setStrokeWidth(size * cw);
p_circle_2.setStrokeWidth(size * cw);
p_circle_3.setStrokeWidth(size * cw);
p_circle_4.setStrokeWidth(size * cw);
p_circles.setStrokeWidth(size * lw/2);

p_circle_1.setShader(new SweepGradient(cx, cy, picker_colors, null));
p_text.setTextSize(size * 0.045f);
p_text.setColor(Color.WHITE);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод малювання результату

@Override
protected void onDraw (Canvas c) {

super.onDraw(c);

draw_Satur_Circle(c);
draw_Alpha_Circle(c);
draw_Color_Circle(c);
draw_Lines(c);

if (show_result) { draw_Result_Circle(c); }
if (show_text)   { draw_Text(c); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private void draw_Color_Circle (Canvas c) {

c.drawCircle(cx, cy, radius_1, p_circle_1);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private void draw_Satur_Circle (Canvas c) {

hsv[0] = angle_1;

int[] array = new int[] { Color.HSVToColor(new float[] { hsv[0], 1, 0    }),
                          Color.HSVToColor(new float[] { hsv[0], 1, 1    }),
                          Color.HSVToColor(new float[] { 0,      0, 1    } ),
                          Color.HSVToColor(new float[] { 0,      0, 0.5f } ),
                          Color.HSVToColor(new float[] { hsv[0], 1, 0    }) };

sg = new SweepGradient(cx, cy, array, null);
p_circle_2.setShader(sg);

c.drawCircle(cx, cy, radius_2, p_circle_2);
set_Satur_Value();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private void draw_Alpha_Circle (Canvas c) {

c.drawCircle(cx, cy, radius_3 - size * lw * 2, p_circles);
c.drawCircle(cx, cy, radius_3,                 p_circles);
c.drawCircle(cx, cy, radius_3 + size * lw * 2, p_circles);

color = Color.HSVToColor(hsv);

int e = Color.argb(0, Color.red(color), Color.green(color), Color.blue(color));
int[] mCol = new int[] {color, e };

Shader sw = new SweepGradient(cx, cy, mCol, null);
p_circle_3.setShader(sw);
c.drawCircle(cx, cy, radius_3, p_circle_3);

set_Alpha_Value();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private void draw_Result_Circle (Canvas c) {

p_circle_4.setColor(Color.HSVToColor(alpha, hsv));
c.drawCircle(cx, cy, radius_4, p_circle_4);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private void draw_Lines (Canvas c) {

rotate_angle = angle_1;
c.rotate(rotate_angle, cx, cy);
c.drawLine(cx + radius_1 + size * 0.05f, cy, cx + radius_1 - size * 0.05f, cy, p_line);
c.rotate(-rotate_angle, cx, cy);

rotate_angle = angle_2;
c.rotate(rotate_angle, cx, cy);
c.drawLine(cx + radius_2 + size * 0.05f, cy, cx + radius_2 - size * 0.05f, cy, p_line);
c.rotate(-rotate_angle, cx, cy);

rotate_angle = angle_3;
c.rotate(rotate_angle, cx, cy);
c.drawLine(cx + radius_3 + size * 0.05f, cy, cx + radius_3 - size * 0.05f, cy, p_line);
c.rotate(-rotate_angle, cx, cy);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private void draw_Text (Canvas c) {

String hex = "0x" + Integer.toHexString(color).toUpperCase();
p_text.setColor((0xffffffff - color) | 0xff000000);

c.drawText(hex, cx, cy + p_text.getTextSize()/2, p_text);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public boolean onTouch (View view, MotionEvent event) {

switch (event.getAction()) {

///////////////////////////////////////////////////////////////////////////////////////////////////

case MotionEvent.ACTION_DOWN:

double rad = Math.sqrt(Math.pow(cx - event.getX(), 2) + Math.pow(cy - event.getY(), 2));

if      (rad > radius_1 - size * cw/2 && rad < radius_1 + size * cw/2) { cMode = SET_COLOR; }
else if (rad > radius_2 - size * cw/2 && rad < radius_2 + size * cw/2) { cMode = SET_SATUR; }
else if (rad > radius_3 - size * cw/2 && rad < radius_3 + size * cw/2) { cMode = SET_ALPHA; }
else if (rad < radius_4) { cMode = -1;
                           if (listener != null) { listener.onDismiss(Color.
                                                            HSVToColor(alpha, hsv)); } }
else                     { cMode = -1; }

switch (cMode) {

    case SET_COLOR: angle_1 = get_Angle(cx - event.getX(), cy - event.getY()); break;
    case SET_SATUR: angle_2 = get_Angle(cx - event.getX(), cy - event.getY()); break;
    case SET_ALPHA: angle_3 = get_Angle(cx - event.getX(), cy - event.getY()); break;

}

if (listener != null) { listener.onColorChanged(Color.HSVToColor(alpha, hsv)); }
break;

///////////////////////////////////////////////////////////////////////////////////////////////////

case MotionEvent.ACTION_MOVE:

switch (cMode) {

    case SET_COLOR: angle_1 = get_Angle(cx - event.getX(), cy - event.getY()); break;
    case SET_SATUR: angle_2 = get_Angle(cx - event.getX(), cy - event.getY()); break;
    case SET_ALPHA: angle_3 = get_Angle(cx - event.getX(), cy - event.getY()); break;

}

if (listener != null) { listener.onColorChanged(Color.HSVToColor(alpha, hsv)); }
break;

}

invalidate();
return true;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private void set_Satur_Value() {

tmp_value = angle_2;

if (tmp_value < 90) { hsv[1] = 1;
                      hsv[2] = tmp_value / 90; }

else if (tmp_value >= 90 && tmp_value <= 180) { hsv[1] = 1 - (tmp_value - 90) / 90;
                                                hsv[2] = 1; }

else { hsv[1] = 0;
       hsv[2] = 1 - (tmp_value - 180) / 180; }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private void set_Alpha_Value() {

tmp_value = angle_3;

alpha = (int) (255 - tmp_value / 360 * 255);
color = Color.HSVToColor(alpha, hsv);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private float get_Angle (float x, float y) {

float deg = 0;
if (x != 0) deg = y / x;
deg = (float) Math.toDegrees(Math.atan(deg));

if (x >= 0 && y >= 0) { deg = 180 + deg; }
if (x >= 0 && y < 0)  { deg = 180 + deg; }
if (x < 0  && y >= 0) { deg = 360 + deg; }
if (x == 0 && y < 0)  { deg = 90;        }
if (x == 0 && y > 0)  { deg = 270;       }

return deg;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private int measure (int measureSpec) {

int result = 0;
int specMoge = MeasureSpec.getMode(measureSpec);
int specSize = MeasureSpec.getSize(measureSpec);

if (specMoge == MeasureSpec.UNSPECIFIED) { result = 1000; }
else                                     { result = specSize; }

return result;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public void set_Color (int color) {

float deg = 0;
Color.colorToHSV(color, hsv);

if      (hsv[1] == 1) { deg = 90 * hsv[2];        }
else if (hsv[2] == 1) { deg = 180 - 90 * hsv[1];  }
else if (hsv[1] == 0) { deg = 360 - 180 * hsv[2]; }
else                  { deg = 90; }

angle_1 = hsv[0];
angle_2 = deg;
angle_3 = 360 - Color.alpha(color) * 360 /255;

invalidate();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public void set_Rotate_Angles (float a1, float a2, float a3) {

angle_1 = (a1 < 0 || a1 >= 360) ? 0 : a1;
angle_2 = (a1 < 0 || a1 >= 360) ? 0 : a1;
angle_3 = (a1 < 0 || a1 >= 360) ? 0 : a1;

invalidate();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public void set_Color_Angle (float value) {

angle_1 = (value < 0 || value >= 360) ? 0 : value;
invalidate();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public void set_Setur_Angle (float value) {

angle_2 = (value < 0 || value >= 360) ? 0 : value;
invalidate();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public void set_Alpha_Angle (float value) {

angle_3 = (value < 0 || value >= 360) ? 0 : value;
invalidate();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public void show_Text (boolean show) { show_text = show; }

///////////////////////////////////////////////////////////////////////////////////////////////////

public void show_Result (boolean show) { show_result = show; }

///////////////////////////////////////////////////////////////////////////////////////////////////

}