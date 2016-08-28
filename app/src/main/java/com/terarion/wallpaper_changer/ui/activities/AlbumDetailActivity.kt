package com.terarion.wallpaper_changer.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.terarion.wallpaper_changer.R
import com.terarion.wallpaper_changer.WallpaperChangerReceiver
import com.terarion.wallpaper_changer.model.DataHolder
import com.terarion.wallpaper_changer.util.delegators.view

/**
 * Created by david on 8/14/16.
 */
class AlbumDetailActivity() : AppCompatActivity() {
    val data = DataHolder()
    val name by lazy { intent.getStringExtra("title") }
    val album by lazy { data.albums.find { it.name == name }!! }

    val recycler by view(RecyclerView::class.java)

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image by view(ImageView::class.java)
    }

    inner class DataAdapter : RecyclerView.Adapter<ImageHolder>() {
        override fun onBindViewHolder(holder: ImageHolder, position: Int) {
            val image = album.images[position]

            Picasso.with(this@AlbumDetailActivity).load(image.file).into(holder.image)

            holder.image.setOnClickListener {
                val intent = Intent(this@AlbumDetailActivity, WallpaperChangerReceiver::class.java)
                intent.putExtra("file", image.file.absolutePath)
                sendBroadcast(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
                ImageHolder(LayoutInflater.from(this@AlbumDetailActivity)
                        .inflate(R.layout.fragment_image, parent, false))

        override fun getItemCount() = album.images.size

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_album_detail)

        recycler.adapter = DataAdapter()
        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.itemAnimator = DefaultItemAnimator()
    }
}