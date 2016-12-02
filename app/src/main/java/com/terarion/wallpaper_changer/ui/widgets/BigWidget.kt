package com.terarion.wallpaper_changer.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.terarion.wallpaper_changer.receivers.InvertReceiver
import com.terarion.wallpaper_changer.R
import com.terarion.wallpaper_changer.receivers.PlayPauseReceiver
import com.terarion.wallpaper_changer.receivers.NextReceiver

/**
 * Created by david on 12/2/16.
 */
class BigWidget : AppWidgetProvider(){
    companion object {
        val PREVIOUS = "previous"
        val NEXT = "next"
        val PLAYPAUSE = "playpause"
        val INVERT = "invert"
        val tag = "WIDGET_BIG"
        var lastRequestCode = 1
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        Log.d(tag, "Updating big widget")

        val remoteViews = RemoteViews(context.packageName, R.layout.widget_big)
        val watchWidget = ComponentName(context, BigWidget::class.java)

        remoteViews.setOnClickPendingIntent(R.id.widget_invert, getPendingIntent(context, INVERT))
        remoteViews.setOnClickPendingIntent(R.id.widget_next, getPendingIntent(context, NEXT))
        remoteViews.setOnClickPendingIntent(R.id.widget_pauseplay, getPendingIntent(context, PLAYPAUSE))

        appWidgetManager.updateAppWidget(watchWidget, remoteViews)

        Log.d(tag, "Big widget update done")
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        Log.d(tag, "Received pending intent")

        context.sendBroadcast(pendingIntentCreator(context, intent))
    }

    private fun pendingIntentCreator(context: Context, intent: Intent) =
            when(intent.action) {
                INVERT -> Intent(context, InvertReceiver::class.java)
                NEXT -> Intent(context, NextReceiver::class.java)
                PLAYPAUSE -> Intent(context, PlayPauseReceiver::class.java)
                else -> Intent(context, NextReceiver::class.java)
            }

    fun getPendingIntent(context: Context, action: String): PendingIntent {
        Log.d(tag, "Creating new wallpaper intent: ${action}")
        val intent = Intent(context, javaClass)
        intent.action = action
        return PendingIntent.getBroadcast(context, lastRequestCode++, intent, 0)
    }
}