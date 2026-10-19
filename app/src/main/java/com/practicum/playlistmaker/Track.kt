package com.practicum.playlistmaker

import java.time.OffsetDateTime

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: Long,
    val artworkUrl: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?
) {
    val coverArtwork : String?
        get() {
        return if (artworkUrl.isNullOrBlank()) {
            null
        } else {
            artworkUrl.replaceAfterLast('/',"512x512bb.jpg")
        }
    }

    val durationInSeconds : Int
        get() = (trackTime / 1000).toInt()

    val durationInString : String
        get() {
            val minutes = durationInSeconds / 60
            val seconds = durationInSeconds % 60
            return "${minutes}:${seconds.toString().padStart(2,'0')}"
        }

    val releaseYear : String?
        get() {
            return if (releaseDate.isNullOrBlank()) {
                null
            } else {
                runCatching {
                    OffsetDateTime.parse(releaseDate).year.toString()
                }.getOrNull()
            }
        }
}