package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<ItemsViewHolder> ()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewTypr: Int): ItemsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return ItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int = tracks.size

}