package com.terarion.wallpaper_changer.model

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.Serializable

/**
 * Created by david on 8/12/16.
 */
class DataHolder : Serializable {
    companion object {
        val BASE_DIR = File(Environment.getExternalStorageDirectory(), "WallpaperChanger")
    }


    var albums = emptyList<Album>()
    val albumNames by lazy { albums.map { it.name } }

    val images by lazy { albums.filter { it.enabled }.map { it.images }.reduceRight { left, right -> left + right } }

    constructor() {
        update()
    }

    private val s = "DATA"

    fun update() {
        try {
            albums = BASE_DIR.listFiles().filter { it.isDirectory }.map { Album(it) }
            Log.d("DATA", "Loaded ${albums.size} albums")
        } catch (throwable: Throwable) {
            Log.d("DATA", "No albums yet")
        }
    }
}

class Album(val directory: File) {
    var enabled: Boolean get() = directory.listFiles().find { it.name == "enabled" } != null
        set(value) = Unit.apply { if (value == true) File(directory, "enabled").mkdir() else File(directory, "enabled").delete() }

    val images by lazy { directory.listFiles().filter { listOf("jpg", "jpeg", "png").contains(it.extension) }.map { Image(it) } }
    val name = directory.name
}

class Image(val file: File) {
    fun delete() {
        file.delete()
    }
}

