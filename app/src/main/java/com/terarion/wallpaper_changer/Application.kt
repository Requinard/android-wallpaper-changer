package com.terarion.wallpaper_changer

import android.app.Application
import com.karumi.dexter.Dexter
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by david on 8/15/16.
 */
class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Dexter.initialize(this)
        JodaTimeAndroid.init(this)
    }
}