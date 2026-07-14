package com.example.mynewcity.view

import com.example.mynewcity.model.GridCell

// stellt dem MainController und dem UIManager die Kartenansicht zur
// Anzeige und Steuerung bereit
interface MapViewProvider {

    fun setupMap()

    fun resume()

    fun pause()

    fun getMapCenter(): Pair<Double, Double>

    fun centerOn(lat: Double, lon: Double)

    fun initializeGrid(allCells: Set<GridCell>)

    fun updateVisited()

    fun clearGrid()

    fun updateLocationMarker(lat: Double, lon: Double)

    fun clearLocationMarker()
}