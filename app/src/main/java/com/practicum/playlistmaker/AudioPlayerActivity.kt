package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson

class AudioPlayerActivity : AppCompatActivity() {

    val gson by lazy { Gson() }

    companion object {
        const val EXTRA_TRACK = "extra_track"
    }

    lateinit var trackImage : ImageView
    lateinit var trackName : TextView
    lateinit var trackArtist : TextView
    lateinit var valueLength : TextView
    lateinit var collectionName : Group
    lateinit var valueAlbum : TextView
    lateinit var releaseDate : Group
    lateinit var valueYear : TextView
    lateinit var valueGenre : TextView
    lateinit var valueCountry : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audioplayer)

        trackImage = findViewById(R.id.trackImage)
        trackName = findViewById(R.id.trackName)
        trackArtist = findViewById(R.id.trackArtist)
        valueLength = findViewById(R.id.valueLength)
        collectionName = findViewById(R.id.collectionName)
        valueAlbum = findViewById(R.id.valueAlbum)
        releaseDate = findViewById(R.id.releaseDate)
        valueYear = findViewById(R.id.valueYear)
        valueGenre = findViewById(R.id.valueGenre)
        valueCountry = findViewById(R.id.valueCountry)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val extraData = intent.getStringExtra(EXTRA_TRACK)
        if (extraData.isNullOrBlank()) {
            finish()
            return
        }

        val btnBack = findViewById<ImageButton>(R.id.backButton)
        btnBack.setOnClickListener { finish() }

        if (!fillContent(extraData)) {
            finish()
            return
        }
    }

     private fun fillContent(data: String?) : Boolean {
        return runCatching {
            val track: Track = gson.fromJson(data, Track::class.java) ?: return false

            Glide.with(this)
                .load(track.coverArtwork)
                .centerCrop()
                .placeholder(R.drawable.placeholder_312)
                .error(R.drawable.placeholder_312)
                .transform(RoundedCorners(dpToPx(8.0f, trackImage)))
                .into(trackImage)

            trackName.text = track.trackName
            trackArtist.text = track.artistName
            valueLength.text = track.durationInString
            valueGenre.text = track.primaryGenreName ?: "-"
            valueCountry.text = track.country ?: "-"

            collectionName.visibility = if (track.collectionName.isNullOrBlank()) {
                android.view.View.GONE
            } else {
                valueAlbum.text = track.collectionName
                android.view.View.VISIBLE
            }
            releaseDate.visibility = if (track.releaseYear.isNullOrBlank()) {
                android.view.View.GONE
            } else {
                valueYear.text = track.releaseYear
                android.view.View.VISIBLE
            }
            return true
        }.getOrNull() ?: false
    }
}