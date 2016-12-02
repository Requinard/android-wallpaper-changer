package com.terarion.wallpaper_changer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast

/**
 * Created by david on 12/2/16.
 */
class PlayPauseReceiver : BroadcastReceiver() {
    val TAG = "Receiver PlayPause"
    override fun onReceive(context: Context, p1: Intent?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        Log.d(TAG, "Toggling wallpaper switching!")
        val current = sharedPreferences.getBoolean("enabled", false)

        Log.d(TAG, "Wallpaper switching is currenty ${current}")
        val editor = sharedPreferences.edit()

        editor.putBoolean("enabled", !current)
        editor.commit()

        Log.d(TAG, "Wallpaper switching is now set to ${!current}")
        if(!current) {
            Toast.makeText(context, "Automatic switching enabled!", Toast.LENGTH_LONG)
        } else{
            Toast.makeText(context, "Automatic switching disabled!", Toast.LENGTH_LONG)
        }
    }

}