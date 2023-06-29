package com.amit.radiuscompose

import android.app.Application
import android.text.style.TtsSpan.TimeBuilder
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RadiusApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}