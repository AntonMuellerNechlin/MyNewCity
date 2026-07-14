package com.example.mynewcity.location

import android.os.Handler
import android.os.Looper

class FakeLocationSource : LocationProvider {

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var callback: (LocationData) -> Unit

    private var startLat = 52.5200
    private var startLon = 13.4050

    private var gridX = 0
    private var gridY = 0

    private val step = 2

    // unabhängig von GridConfig.CELL_SIZE_METERS - nur die künstliche
    // Schrittgröße der Fake-Bewegung in Grad
    private val cellSize = 0.0001

    private val runnable = object : Runnable {
        override fun run() {
            gridX += step
            gridY += step

            val lat = startLat + (gridY * cellSize)
            val lon = startLon + (gridX * cellSize)

            callback(LocationData(lat, lon))

            handler.postDelayed(this, 2000)
        }
    }

    override fun setStartPosition(lat: Double, lon: Double) {
        startLat = lat
        startLon = lon
        gridX = 0
        gridY = 0
    }

    override fun start(onLocationUpdate: (LocationData) -> Unit) {
        callback = onLocationUpdate
        handler.post(runnable)
    }

    override fun stop() {
        handler.removeCallbacks(runnable)
    }
}