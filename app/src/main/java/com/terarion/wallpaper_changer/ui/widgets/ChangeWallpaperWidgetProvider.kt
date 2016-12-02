package com.terarion.wallpaper_changer.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.terarion.wallpaper_changer.R
import com.terarion.wallpaper_changer.receivers.NextReceiver

/**
 * Created by david on 8/15/16.
 */
class ChangeWallpaperWidgetProvider : AppWidgetProvider() {
    val name = "WALLPAPER CHANGE"
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val remoteViews = RemoteViews(context.packageName, R.layout.widget_change_wallpaper)
        val watchWidget = ComponentName(context, ChangeWallpaperWidgetProvider::class.java)

        remoteViews.setOnClickPendingIntent(R.id.widget_change, getPendingIntent(context, name))
        appWidgetManager.updateAppWidget(watchWidget, remoteViews)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        Log.d(tag, "Received pending intent")
        if (intent.action == name) {
            val intent = Intent(context, NextReceiver::class.java)
            Log.d(tag, "broadcasting wallpaper change")

            context.sendBroadcast(intent)
        }
    }

    private val tag = "Widget"

    fun getPendingIntent(context: Context, action: String): PendingIntent {
        Log.d(tag, "Creating new wallpaper pending intent")
        val intent = Intent(context, javaClass)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}