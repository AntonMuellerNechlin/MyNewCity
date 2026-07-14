package com.example.mynewcity.view

import com.example.mynewcity.model.GridCell
import com.example.mynewcity.model.GridConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MapRenderer(
    private val map: MapView,
    private val mapDataProvider: MapDataProvider,
    private val gridVisualizationProvider: GridVisualizationProvider,
    private val locationOverlay: LocationOverlay
) : MapViewProvider {

    override fun setupMap() {
        mapDataProvider.configureMap(map)
        centerMap()
    }

    fun centerMap() {
        centerOn(GridConfig.ORIGIN_LAT, GridConfig.ORIGIN_LON)
    }

    override fun centerOn(lat: Double, lon: Double) {
        map.controller.setCenter(GeoPoint(lat, lon))
    }

    override fun getMapCenter(): Pair<Double, Double> {
        val center = map.mapCenter
        return center.latitude to center.longitude
    }

    override fun initializeGrid(allCells: Set<GridCell>) {
        gridVisualizationProvider.initializeGrid(allCells)
    }

    override fun updateVisited(visitedCells: Set<GridCell>) {
        gridVisualizationProvider.updateVisited(visitedCells)
    }

    override fun clearGrid() {
        gridVisualizationProvider.clearGrid()
    }

    override fun updateLocationMarker(lat: Double, lon: Double) {
        locationOverlay.updateLocation(lat, lon)
    }

    override fun clearLocationMarker() {
        locationOverlay.clearLocation()
    }

    override fun resume() {
        map.onResume()
    }

    override fun pause() {
        map.onPause()
    }
}
