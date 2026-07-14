package com.example.mynewcity.model

// wird vom MainController genutzt, um Standortdaten an den GridManager
// zu übergeben und daraus Rasterdaten berechnen/aktualisieren zu lassen
interface GridUpdateProvider {

    fun addLocation(lat: Double, lon: Double): GridCell

    fun getVisitedCount(): Int

    fun toGridCell(lat: Double, lon: Double): GridCell

    fun generateGrid(
        centerLat: Double,
        centerLon: Double,
        radius: Int
    ): Set<GridCell>

    fun reset()
}