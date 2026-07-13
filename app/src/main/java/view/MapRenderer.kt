package com.example.mynewcity.view

import com.example.mynewcity.model.GridCell
import com.example.mynewcity.model.GridConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MapRenderer(
    private val map: MapView,
    private val gridOverlay: GridOverlay
) : MapViewProvider {

    override fun setupMap() {
        map.setMultiTouchControls(true)
        map.controller.setZoom(18.0)
        centerMap()
    }

    fun centerMap() {
        val startPoint = GeoPoint(
            GridConfig.ORIGIN_LAT,
            GridConfig.ORIGIN_LON
        )

        map.controller.setCenter(startPoint)
    }

    fun renderGrid(
        allCells: Set<GridCell>,
        visitedCells: Set<GridCell>
    ) {
        gridOverlay.setVisited(visitedCells)
        gridOverlay.drawGrid(allCells)
    }

    override fun resume() {
        map.onResume()
    }

    override fun pause() {
        map.onPause()
    }
}