package com.terarion.wallpaper_changer

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import com.terarion.wallpaper_changer.model.DataHolder
import com.terarion.wallpaper_changer.model.Image
import org.joda.time.DateTime
import java.io.File
import java.util.*

/**
 * Created by david on 8/15/16.
 */
class WallpaperChangerReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(tag, "Woken up for intent")


        val random = Random()
        val data = DataHolder()
        val nextImage = if (intent.hasExtra("file")) Image(File(intent.getStringExtra("file"))) else data.images[random.nextInt(data.images.size)]

        Log.d(tag, "Next image is ${nextImage.file.name}")


        val wallpaperManager = WallpaperManager.getInstance(context)

        wallpaperManager.setStream(nextImage.file.inputStream())

        WallpaperChangerReceiver().schedule(context)
    }

    private val tag = "Wakeup service"

    fun schedule(context: Context) {
        // First we cancel any alarms
        cancel(context)

        // Then we get some settings
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val minutes_offset = preferences.getString("interval", "15").toInt()
        val enabled = preferences.getBoolean("enabled", false)

        Log.d(tag, "Scheduling wakeup in $minutes_offset minutes")

        // Now we set the intent to execute
        val intent = Intent(context, WallpaperChangerReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, 101, intent, 0)

        // We set the wakeup time
        val time = DateTime.now().plusMinutes(minutes_offset)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // If we are enabled, we actually set the change
        if (enabled) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time.millis, pendingIntent)
            Log.d(tag, "scheduled next wakeup")
        } else {
            Log.d(tag, "No wakeup has been scheduled")
        }
    }

    fun cancel(context: Context) {
        val intent = Intent(context, WallpaperChangerReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, 101, intent, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
    }

}