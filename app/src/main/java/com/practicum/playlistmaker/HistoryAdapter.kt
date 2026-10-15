package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    private val tracks: List<Track>,
    private val onItemClick: (Track) -> Unit
) : RecyclerView.Adapter<ItemsViewHolder> ()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewTypr: Int): ItemsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return ItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onItemClick(track)
        }
    }

    override fun getItemCount(): Int = tracks.size

}