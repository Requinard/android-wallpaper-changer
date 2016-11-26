package com.terarion.wallpaper_changer.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.terarion.wallpaper_changer.InvertAlbumReceiver
import com.terarion.wallpaper_changer.R
import com.terarion.wallpaper_changer.WallpaperChangerReceiver

/**
 * Created by david on 9/28/16.
 */
class InvertAlbumWidgetProvider : AppWidgetProvider() {
    val name = "ALBUM_INVERTER_WIDGET"
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val remoteViews = RemoteViews(context.packageName, R.layout.widget_invert_albums)
        val watchWidget = ComponentName(context, InvertAlbumWidgetProvider::class.java)

        remoteViews.setOnClickPendingIntent(R.id.widget_invert, getPendingIntent(context, name))
        appWidgetManager.updateAppWidget(watchWidget, remoteViews)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        Log.d(tag, "Received pending intent")
        if (intent.action == name) {
            val intent = Intent(context, InvertAlbumReceiver::class.java)
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