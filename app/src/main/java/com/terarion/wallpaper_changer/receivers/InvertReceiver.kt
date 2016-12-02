package com.terarion.wallpaper_changer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.terarion.wallpaper_changer.model.DataHolder

/**
 * Inverts all albums
 */
class InvertReceiver : BroadcastReceiver() {
    private val LOG_TAG = "INVERTER"

    override fun onReceive(context: Context?, intent: Intent?) {
        val data = DataHolder()
        Log.d(LOG_TAG, "Inverting albums")
        data.albums.forEach { it.enabled = !it.enabled }
        Log.d(LOG_TAG, "Inverted albums")

        Toast.makeText(context, "Inverted the album selection!", Toast.LENGTH_LONG)
    }
}