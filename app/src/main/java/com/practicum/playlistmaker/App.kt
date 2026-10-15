package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val PM_PREFERENCES = "playlistmaker_preferencses"

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        Prefs.init(this)
        darkTheme = Prefs.darkTheme
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        if (darkTheme != darkThemeEnabled) {
            Prefs.darkTheme = darkThemeEnabled
            darkTheme = darkThemeEnabled
        }
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}