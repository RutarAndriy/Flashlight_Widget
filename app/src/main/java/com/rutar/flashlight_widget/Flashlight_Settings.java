package com.rutar.flashlight_widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Flashlight_Settings extends Activity {

private Color_Picker picker;
public static Flashlight_Settings settings;

private int color_index = 0;

private int rotate_angle = 45;

private int[] colors = new int[] { Color.RED, Color.GREEN, Color.BLUE,
                                   Color.YELLOW, Color.CYAN, Color.MAGENTA,
                                   Color.BLACK, Color.WHITE,
                                   Color.LTGRAY, Color.GRAY, Color.DKGRAY,
                                   Color.argb(123, 23,  0,   72),
                                   Color.argb(13,  203, 19,  12),
                                   Color.argb(213, 123, 223, 193) };

private View viewById;

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected void onCreate (Bundle savedInstanceState) {

super.onCreate(savedInstanceState);
settings = this;

requestWindowFeature(Window.FEATURE_NO_TITLE);
setContentView(R.layout.settings);

/*viewById = findViewById(R.id.test);
viewById.setBackgroundColor(Color.BLUE);*/

picker = (Color_Picker) findViewById(R.id.view);
picker.show_Result(true);
picker.show_Text(true);
picker.set_Color_Angle(90);
picker.set_Setur_Angle(90);
picker.set_Alpha_Angle(45);
//picker.set_Color(colors[color_index]);

onPreDraw();

picker.setOnColorChangeListener(new Color_Picker.OnColorChangeListener() {
    @Override
    public void onDismiss(int color) {
        Toast.makeText(Flashlight_Settings.this, "Color: 0x" + Integer.
                       toHexString(color).toUpperCase(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onColorChanged(int color) {

        //viewById.setBackgroundColor(color);

    }
});

}

    private void onPreDraw() {

        picker.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                final int[] id = new int[] { R.id.next, R.id.prev, R.id.left, R.id.right };

                for (int z = 0; z < id.length; z++) {

                    ImageView imageView = (ImageView) findViewById(id[z]);
                    ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                    layoutParams.height = (int) (picker.getMeasuredHeight() * 0.18f);
                    layoutParams.width = (int) (picker.getMeasuredWidth() * 0.18f);
                    imageView.setLayoutParams(layoutParams);

                }

                //Remove it here unless you want to get this callback for EVERY
                //layout pass, which can get you into infinite loops if you ever
                //modify the layout from within this method.
                picker.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                //Now you can get the width and height from content
            }
        });

    }

///////////////////////////////////////////////////////////////////////////////////////////////////



public void view_Press (View v) {

if (v.getId() == R.id.next)  { Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show(); }
if (v.getId() == R.id.prev)  { Toast.makeText(this, "Previous", Toast.LENGTH_SHORT).show(); }

if (v.getId() == R.id.left)  { rotate_angle -= rotate_angle > 0   ? 5 : -355; }
if (v.getId() == R.id.right) { rotate_angle += rotate_angle < 355 ? 5 : -355; }

Toast.makeText(this, "Rotate Angle: " + rotate_angle, Toast.LENGTH_SHORT).show();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}