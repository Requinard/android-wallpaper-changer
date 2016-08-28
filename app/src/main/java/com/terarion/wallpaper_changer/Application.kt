package com.terarion.wallpaper_changer

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by david on 8/15/16.
 */
class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}