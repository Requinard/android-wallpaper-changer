package com.terarion.wallpaper_changer.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.terarion.wallpaper_changer.R
import com.terarion.wallpaper_changer.model.DataHolder
import com.terarion.wallpaper_changer.ui.activities.AlbumDetailActivity
import com.terarion.wallpaper_changer.util.delegators.view

/**
 * Created by david on 8/14/16.
 */
class AlbumRecyclerFragment() : Fragment() {
    val data = DataHolder()
    val holder by lazy { activity.findViewById(R.id.albumRecycler) as RecyclerView }

    // Event view holder finds and memorizes the views in an event card
    inner class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image by view(ImageView::class.java)
        val title by view(CheckedTextView::class.java)
    }

    inner class DataAdapter : RecyclerView.Adapter<AlbumViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                AlbumViewHolder(LayoutInflater.
                        from(parent.context)
                        .inflate(R.layout.fragment_album_item, parent, false))

        override fun getItemCount() = data.albums.size

        override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
            val album = data.albums[position]
            holder.title.text = album.name
            holder.title.isChecked = album.enabled
            try {
                Picasso.with(activity).load(album.images.first().file).into(holder.image)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }

            holder.title.setOnClickListener {
                album.enabled = !holder.title.isChecked
                this.notifyDataSetChanged()
            }

            holder.image.setOnClickListener {
                val intent = Intent(context, AlbumDetailActivity::class.java)
                intent.putExtra("title", album.name)
                startActivity(intent)
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_album_recycler, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        holder.adapter = DataAdapter()
        holder.layoutManager = LinearLayoutManager(activity)
        holder.setHasFixedSize(true)
        holder.itemAnimator = DefaultItemAnimator()

        holder.adapter.notifyDataSetChanged()
    }
}