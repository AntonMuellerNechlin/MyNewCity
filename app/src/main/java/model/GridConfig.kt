package com.example.mynewcity.model

import org.osmdroid.util.GeoPoint

object GridConfig {

    const val BASE_CELL_SIZE_METERS = 10.0
    const val SCALE = 9.0

    const val ORIGIN_LAT = 52.5200
    const val ORIGIN_LON = 13.4050

    val CELL_SIZE_METERS = BASE_CELL_SIZE_METERS * SCALE
}