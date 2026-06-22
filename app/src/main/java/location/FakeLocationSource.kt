package com.example.mynewcity.location

import android.os.Handler
import android.os.Looper

class FakeLocationSource : LocationProvider {

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var callback: (LocationData) -> Unit

    // Startpunkt (Berlin)
    private val startLat = 52.5200
    private val startLon = 13.4050

    // aktuelle „Grid-Position“
    private var gridX = 0
    private var gridY = 0

    // Schrittgröße in GRID Zellen
    private val step = 1

    // künstliche Schrittgröße für die Fake-GPS-Bewegung
    private val cellSize = 0.0001

    private val runnable = object : Runnable {
        override fun run() {

            // 👉 Bewegung im Grid
            gridX += step
            gridY += step

            val lat = startLat + (gridY * cellSize)
            val lon = startLon + (gridX * cellSize)

            callback(LocationData(lat, lon))

            handler.postDelayed(this, 2000)
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