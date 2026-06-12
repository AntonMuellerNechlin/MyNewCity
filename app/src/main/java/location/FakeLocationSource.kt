package com.example.mynewcity.location

import android.os.Handler
import android.os.Looper

class FakeLocationSource : LocationSource {

    private val handler = Handler(Looper.getMainLooper())

    private var index = 0

    private lateinit var callback: (LocationData) -> Unit

    private val route = listOf(
        LocationData(52.5200, 13.4050),
        LocationData(52.5201, 13.4052),
        LocationData(52.5202, 13.4054),
        LocationData(52.5203, 13.4056),
        LocationData(52.5204, 13.4058)
    )

    private val runnable = object : Runnable {
        override fun run() {

            callback(route[index])

            index = (index + 1) % route.size

            handler.postDelayed(this, 2000) // alle 2 Sekunden
        }
    }

    override fun start(onLocationUpdate: (LocationData) -> Unit) {
        callback = onLocationUpdate
        handler.post(runnable)
    }

    override fun stop() {
        handler.removeCallbacks(runnable)
    }
}