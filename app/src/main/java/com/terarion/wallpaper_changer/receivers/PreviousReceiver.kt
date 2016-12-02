package com.terarion.wallpaper_changer.receivers

import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.terarion.wallpaper_changer.store.HistoryStore

/**
 * Created by david on 12/2/16.
 */
class PreviousReceiver : BroadcastReceiver() {
    val TAG = "PreviousReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        val image = HistoryStore().revert()

        Log.d(TAG, "Reverting to last image")
        if (image != null) {
            val wallpaperManager = WallpaperManager.getInstance(context)

            wallpaperManager.setStream(image.file.inputStream())

            NextReceiver().schedule(context)

            Log.d(TAG, "Succesfully reverted image")
        } else {
            Log.d(TAG, "Failed to revert wallpaper image: None found!")
        }
    }
}