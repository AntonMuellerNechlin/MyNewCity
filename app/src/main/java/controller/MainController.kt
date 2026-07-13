package com.example.mynewcity.controller

import android.util.Log
import com.example.mynewcity.location.LocationProvider
import com.example.mynewcity.model.GridConfig
import com.example.mynewcity.model.GridManager
import com.example.mynewcity.view.MapRenderer
import com.example.mynewcity.view.UIUpdateProvider

class MainController(
    private val locationProvider: LocationProvider,
    private val gridManager: GridManager,
    private val mapRenderer: MapRenderer,
    private val uiUpdateProvider: UIUpdateProvider,
    private val executeOnUiThread: (() -> Unit) -> Unit
) {

    fun startTracking() {
        uiUpdateProvider.updateTrackingState(true)

        locationProvider.start { location ->

            Log.d(
                "MYNEWCITY_TEST",
                "FakeGPS: ${location.latitude}, ${location.longitude}"
            )

            val cell = gridManager.addLocation(
                location.latitude,
                location.longitude
            )

            Log.d(
                "MYNEWCITY_TEST",
                "GRID: cell=${cell.x}, ${cell.y}"
            )

            Log.d(
                "MYNEWCITY_TEST",
                "Visited cells: ${gridManager.getVisitedCells().size}"
            )

            val visitedCells = gridManager.getVisitedCells()

            val allCells = gridManager.generateGrid(
                GridConfig.ORIGIN_LAT,
                GridConfig.ORIGIN_LON
            )

            val progressPercent = (visitedCells.size * 100) / allCells.size

            executeOnUiThread {
                mapRenderer.renderGrid(
                    allCells,
                    visitedCells
                )
                uiUpdateProvider.updateVisitedCells(visitedCells.size)
                uiUpdateProvider.updateProgress(progressPercent)
            }
        }
    }

    fun stopTracking() {
        locationProvider.stop()
        uiUpdateProvider.updateTrackingState(false)
    }

    fun resetTracking() {
        gridManager.reset()

        val allCells = gridManager.generateGrid(
            GridConfig.ORIGIN_LAT,
            GridConfig.ORIGIN_LON
        )

        executeOnUiThread {
            mapRenderer.renderGrid(
                allCells,
                gridManager.getVisitedCells()
            )
            uiUpdateProvider.updateVisitedCells(0)
            uiUpdateProvider.updateProgress(0)
        }
    }

    fun centerMap() {
        executeOnUiThread {
            mapRenderer.centerMap()
        }
    }
}