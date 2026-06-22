package com.example.mynewcity.controller

import android.util.Log
import com.example.mynewcity.location.LocationProvider
import com.example.mynewcity.model.GridConfig
import com.example.mynewcity.model.GridManager
import com.example.mynewcity.view.MapRenderer

class MainController(
    private val locationProvider: LocationProvider,
    private val gridManager: GridManager,
    private val mapRenderer: MapRenderer,
    private val executeOnUiThread: (() -> Unit) -> Unit
) {

    fun startTracking() {
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

            executeOnUiThread {
                mapRenderer.renderGrid(
                    allCells,
                    visitedCells
                )
            }
        }
    }

    fun stopTracking() {
        locationProvider.stop()
    }
}