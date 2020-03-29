package com.rutar.flashlight_widget;

import android.net.Uri;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.rutar.flashlight_widget.R;

public class Flashlight_Widget extends AppWidgetProvider{

    @Override
    public void onUpdate (Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        for (int i=0; i < appWidgetIds.length; i++) {

            int currentWidgetId = appWidgetIds[i];

            //Делаем простой http запрос на указанную ссылку и выполняем по ней переход:
            String url = "http://learn-android.ru";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(url));

            //Определяем два обязательных объекта класса PendingIntent и RemoteViews:
            PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            //Настраиваем обработку клика по добавлению виджета:
            views.setOnClickPendingIntent(R.id.button, pending);
            appWidgetManager.updateAppWidget(currentWidgetId, views);
            Toast.makeText(context, "Віджет додано", Toast.LENGTH_SHORT).show();
        }
    }
}