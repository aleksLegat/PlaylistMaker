package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameView : TextView = itemView.findViewById(R.id.trackName)
    private var artistNameView : TextView = itemView.findViewById(R.id.artistName)
    private val trackTimeView : TextView = itemView.findViewById(R.id.trackTime)
    private val artworkView : ImageView = itemView.findViewById(R.id.artwork)

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder_45)
            .transform(RoundedCorners(2))
            .into(artworkView)
    }
}