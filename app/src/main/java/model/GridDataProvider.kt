package com.example.mynewcity.model

interface GridDataProvider {

    fun getVisitedCells(): Set<GridCell>

    fun generateGrid(
        centerLat: Double,
        centerLon: Double,
        radius: Int = 20
    ): Set<GridCell>
}