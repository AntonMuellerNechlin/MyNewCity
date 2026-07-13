package com.example.mynewcity.model

import kotlin.math.roundToInt

object GridConfig {

    const val BASE_CELL_SIZE_METERS = 10.0
    const val SCALE = 9.0

    const val CELL_SIZE_METERS = BASE_CELL_SIZE_METERS * SCALE

    const val ORIGIN_LAT = 52.5200
    const val ORIGIN_LON = 13.4050

    // wandelt einen Umkreis in km in eine Zellenanzahl fürs Raster um
    // muss nicht exakt sein, nur ungefähr passend zur echten Kartengröße
    fun radiusCellsFromKm(km: Double): Int {
        val meters = km * 1000.0
        return (meters / CELL_SIZE_METERS).roundToInt()
    }
}