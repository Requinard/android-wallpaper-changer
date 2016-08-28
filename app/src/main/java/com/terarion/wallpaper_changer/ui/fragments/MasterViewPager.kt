package com.terarion.wallpaper_changer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.terarion.wallpaper_changer.R
import com.terarion.wallpaper_changer.util.delegators.view

/**
 * Created by david on 8/14/16.
 */
class MasterViewPager() : Fragment() {
    val viewpager by view(ViewPager::class.java)

    inner class ViewPagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {
        override fun getCount(): Int = 2

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> SettingsFragment()
            else -> AlbumRecyclerFragment()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_master_viewpager, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewpager.adapter = ViewPagerAdapter(fragmentManager)
    }
}