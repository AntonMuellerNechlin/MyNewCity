package com.example.mynewcity.view

import com.example.mynewcity.model.GridCell
import com.example.mynewcity.model.GridConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MapRenderer(
    private val map: MapView,
    private val gridOverlay: GridOverlay
) {

    fun setupMap() {
        map.setMultiTouchControls(true)

        val startPoint = GeoPoint(
            GridConfig.ORIGIN_LAT,
            GridConfig.ORIGIN_LON
        )

        map.controller.setZoom(18.0)
        map.controller.setCenter(startPoint)
    }

    fun renderGrid(
        allCells: Set<GridCell>,
        visitedCells: Set<GridCell>
    ) {
        gridOverlay.setVisited(visitedCells)
        gridOverlay.drawGrid(allCells)
    }

    fun resume() {
        map.onResume()
    }

    fun pause() {
        map.onPause()
    }
}