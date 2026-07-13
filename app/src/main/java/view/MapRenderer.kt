package com.example.mynewcity.view

import com.example.mynewcity.model.GridCell
import com.example.mynewcity.model.GridConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MapRenderer(
    private val map: MapView,
    private val gridOverlay: GridOverlay,
    private val locationOverlay: LocationOverlay
) : MapViewProvider {

    override fun setupMap() {
        map.setMultiTouchControls(true)
        map.controller.setZoom(18.0)
        centerMap()
    }

    fun centerMap() {
        centerOn(GridConfig.ORIGIN_LAT, GridConfig.ORIGIN_LON)
    }

    fun centerOn(lat: Double, lon: Double) {
        map.controller.setCenter(GeoPoint(lat, lon))
    }

    fun getMapCenter(): Pair<Double, Double> {
        val center = map.mapCenter
        return center.latitude to center.longitude
    }

    fun initializeGrid(allCells: Set<GridCell>) {
        gridOverlay.initializeGrid(allCells)
    }

    fun updateVisited(visitedCells: Set<GridCell>) {
        gridOverlay.updateVisited(visitedCells)
    }

    fun clearGrid() {
        gridOverlay.clearGrid()
    }

    fun updateLocationMarker(lat: Double, lon: Double) {
        locationOverlay.updateLocation(lat, lon)
    }

    fun clearLocationMarker() {
        locationOverlay.clearLocation()
    }

    override fun resume() {
        map.onResume()
    }

    override fun pause() {
        map.onPause()
    }
}