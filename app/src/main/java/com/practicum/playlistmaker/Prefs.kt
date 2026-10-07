package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Prefs {

    private lateinit var prefs: SharedPreferences
    private val gson by lazy { Gson() }

    private val tracksType = object : TypeToken<List<Track>>() {}.type

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PM_PREFERENCES, Context.MODE_PRIVATE)
    }

    const val DARK_THEME_STATUS = "dark_theme_status"
    const val TRACKS_HISTORY = "tracks_history"

    var darkTheme : Boolean
        get() = prefs.getBoolean(DARK_THEME_STATUS, false)
        set(value) = prefs.edit().putBoolean(DARK_THEME_STATUS, value).apply()

    var tracksHistory : List<Track>
        get() {
            val json = prefs.getString(TRACKS_HISTORY, null) ?: return emptyList()
            return try {
                gson.fromJson(json, tracksType) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
        set(value) = prefs.edit().putString(TRACKS_HISTORY, gson.toJson(value)).apply()
}