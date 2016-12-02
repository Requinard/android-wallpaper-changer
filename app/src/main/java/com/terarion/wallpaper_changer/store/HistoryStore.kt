package com.terarion.wallpaper_changer.store

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.terarion.wallpaper_changer.model.DataHolder
import com.terarion.wallpaper_changer.model.Image
import com.terarion.wallpaper_changer.util.extensions.limit
import com.terarion.wallpaper_changer.util.extensions.tail
import java.io.File

/**
 * Created by david on 12/2/16.
 */
class HistoryStore {
    val TAG = "HistoryStore"
    private fun getFile() = File(DataHolder.BASE_DIR, "history.json")
    val typeToken = object : TypeToken<List<Image>>() {}

    fun add(image: Image) {
        Log.d(TAG, "Adding ${image} to history")
        var items = read()
                .filter { it.file.name != image.file.name }
        Log.d(TAG, "History has ${items.size} items")
        items = listOf(image) + items
        write(items.limit(50))
    }

    fun revert(): Image? {
        Log.d(TAG, "Retrieving last image from history for reverting")
        val items = read()

        val item = items.firstOrNull()

        write(items.tail())

        return item
    }

    private fun write(items: List<Image>) {
        getFile().writeText(Gson().toJson(items))
        Log.d(TAG, "History now has ${items.size} items")
    }

    fun read(): List<Image> {
        val file = getFile()
        if (!file.exists()) {
            Log.d(TAG, "No history exists yet")
            return emptyList()
        } else {
            try {
                return Gson().fromJson(file.readText(), typeToken.type)
            } catch (throwable: Throwable) {
                Log.d(TAG, "Error reading history")
                return emptyList()
            }
        }
    }
}