package com.example.mynewcity.model

import org.osmdroid.util.GeoPoint

object GridConfig {

    const val BASE_CELL_SIZE_METERS = 10.0
    const val SCALE = 9.0

    val CELL_SIZE_METERS = BASE_CELL_SIZE_METERS * SCALE

    // 🔥 fehlt aktuell bei dir
    val ORIGIN = GeoPoint(52.5200, 13.4050)
}