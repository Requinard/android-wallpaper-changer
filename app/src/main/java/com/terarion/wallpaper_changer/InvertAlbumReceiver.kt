package com.terarion.wallpaper_changer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.terarion.wallpaper_changer.model.DataHolder

/**
 * Created by david on 9/28/16.
 */
class InvertAlbumReceiver: BroadcastReceiver() {
    private val LOG_TAG = "INVERTER"

    override fun onReceive(context: Context?, intent: Intent?) {
        val data = DataHolder()
        Log.d(LOG_TAG, "Inverting albums")
        data.albums.forEach { it.enabled = !it.enabled }
        Log.d(LOG_TAG, "Inverted albums")
    }
}