package com.terarion.wallpaper_changer.ui.fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

import com.terarion.wallpaper_changer.R

/**
 * Created by david on 8/15/16.
 */
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}