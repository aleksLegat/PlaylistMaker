package com.practicum.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    private val retrofit = Retrofit
        .Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val iTunesApiService : ITunesApiService = retrofit.create(ITunesApiService::class.java)

    private var enteredText : CharSequence? = null

    private lateinit var clearIcon : ImageView
    private lateinit var searchEditText : EditText
    private lateinit var btnBack : ImageView
    private lateinit var rvItemView : RecyclerView
    private lateinit var placeholder : LinearLayout
    private lateinit var placeholderImageView : ImageView
    private lateinit var placeholderTextView : TextView
    private lateinit var placeholderButton : Button
    private lateinit var llHistory : LinearLayout
    private lateinit var rvHistory : RecyclerView
    private lateinit var btnClearHistory : Button

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val songsList = arrayListOf<Track>()
    private val tracksAdapter = TracksAdapter(songsList) { track ->
        addTrackToHistory(track)
    }

    private val historyList = arrayListOf<Track>()
    private val historyAdapter = HistoryAdapter(historyList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        clearIcon = findViewById(R.id.clearIcon)
        searchEditText = findViewById(R.id.searchEditText)
        btnBack = findViewById(R.id.btnSearchBack)
        rvItemView = findViewById(R.id.rvTracks)
        placeholder = findViewById(R.id.placeholder)
        placeholderImageView = findViewById(R.id.placeholderImageView)
        placeholderTextView = findViewById(R.id.placeholderTextView)
        placeholderButton = findViewById(R.id.placeholderButton)
        llHistory = findViewById(R.id.llHistory)
        rvHistory = findViewById(R.id.rvHistory)
        btnClearHistory = findViewById(R.id.btnClearHistory)

        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        rvItemView.adapter = tracksAdapter

        btnBack.setOnClickListener {
            finish()
        }

        rvHistory.adapter = historyAdapter
        btnClearHistory.setOnClickListener {
            Prefs.tracksHistory = emptyList()
            showHistory()
        }

        clearIcon.setOnClickListener {
            songsList.clear()
            tracksAdapter.notifyDataSetChanged()
            placeholder.isVisible = false
            searchEditText.setText("")
            searchEditText.clearFocus()
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                clearIcon.isVisible = !s.isNullOrEmpty()
                saveEnteredText(s)
                showHistory()
            }
            override fun afterTextChanged(s: Editable?) { }
        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handled = true
                songsList.clear()
                tracksAdapter.notifyDataSetChanged()

                if (!searchEditText.text.isNullOrEmpty()) {
                    searchSong(searchEditText.text.toString())
                }
            }
            handled
        }

        searchEditText.setOnFocusChangeListener { _, _ ->
            showHistory()
        }

        placeholderButton.setOnClickListener {
            if (!searchEditText.text.isNullOrEmpty()) {
                searchSong(searchEditText.text.toString())
            }
        }

        searchEditText.requestFocus()
    }

    private fun saveEnteredText(s: CharSequence?) {
        enteredText = s
    }

    companion object {
        const val ENTERED_TEXT = "ENTERED_TEXT"
        const val HISTORY_SIZE = 10
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(ENTERED_TEXT, enteredText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        enteredText = savedInstanceState.getCharSequence(ENTERED_TEXT)
        findViewById<EditText>(R.id.searchEditText).setText(enteredText)
    }

    private fun searchSong(textToSearch : String) {
        placeholder.isVisible = false
        iTunesApiService.searchSong("song", textToSearch)
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                    if (response.isSuccessful) {
                        val results = response.body()?.results
                        if (results.isNullOrEmpty()) {
                            showMessage(false)
                        } else {
                            for (tr in results) {
                                songsList.add(
                                    Track(
                                        tr.trackId,
                                        tr.trackName,
                                        tr.artistName,
                                        dateFormat.format(tr.trackTimeMillis),
                                        tr.artworkUrl100
                                    )
                                )
                                tracksAdapter.notifyItemInserted(songsList.size - 1)
                            }
                        }
                    } else {
                        showMessage(true)
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showMessage(true)
                }
            }
        )
    }

    private fun showMessage(isConnectionProblem: Boolean) {
        if (isConnectionProblem) {
            placeholderImageView.setImageResource(R.drawable.connection_problem_120)
            placeholderTextView.setText(R.string.connection_problem)
            placeholderButton.isVisible = true
        } else {
            placeholderImageView.setImageResource(R.drawable.nothing_was_found_120)
            placeholderTextView.setText(R.string.nothing_was_found)
            placeholderButton.isVisible = false
        }
        placeholder.isVisible = true
    }

    private fun addTrackToHistory(track: Track) {
        Prefs.tracksHistory = listOf(track) + Prefs.tracksHistory.filterNot { it.trackId == track.trackId }
            .take(HISTORY_SIZE)
    }

    private fun showHistory() {
        val isActive = searchEditText.hasFocus() && searchEditText.text.isEmpty()
        if (!isActive) {
            llHistory.isVisible = false
            return
        }

        historyList.clear()
        historyList.addAll(Prefs.tracksHistory)
        historyAdapter.notifyDataSetChanged()

        llHistory.isVisible = historyList.isNotEmpty()
    }
}