package com.terarion.wallpaper_changer.util.extensions

/**
 * Created by david on 12/2/16.
 */
fun <T> List<T>.limit(n: Int): List<T> =
        if (this.size > n) this.subList(0, n) else this

fun <T> List<T>.tail(index: Int = 1): List<T> =
        if(this.size == 0) emptyList() else this.subList(1, this.size)