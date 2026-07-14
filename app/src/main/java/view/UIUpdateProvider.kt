package com.example.mynewcity.view

import com.example.mynewcity.model.GridCell

interface UIUpdateProvider {

    fun updateTrackingState(isRunning: Boolean)

    fun updateVisitedCells(count: Int)

    fun updateProgress(percent: Int)

    fun getMapCenter(): Pair<Double, Double>

    fun centerOn(lat: Double, lon: Double)

    fun initializeGrid(allCells: Set<GridCell>)

    fun updateVisited()

    fun clearGrid()

    fun updateLocationMarker(lat: Double, lon: Double)

    fun clearLocationMarker()
}