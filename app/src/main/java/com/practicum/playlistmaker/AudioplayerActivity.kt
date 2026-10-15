package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson

class AudioplayerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TRACK = "extra_track"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audioplayer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val trackData = intent.getStringExtra(EXTRA_TRACK)
        if (trackData.isNullOrBlank()) {
            finish()
            return
        }
        val gson = Gson()
        val track : Track? = gson.fromJson(trackData, Track::class.java)
        if (track == null) {
            finish()
            return
        }

        val btnBack = findViewById<ImageButton>(R.id.backButton)
        btnBack.setOnClickListener {
            finish()
        }

    }
}