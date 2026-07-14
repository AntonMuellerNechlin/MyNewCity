package com.example.mynewcity.controller

import android.util.Log
import com.example.mynewcity.location.LocationData
import com.example.mynewcity.location.LocationProvider
import com.example.mynewcity.model.GridCell
import com.example.mynewcity.model.GridConfig
import com.example.mynewcity.model.GridUpdateProvider
import com.example.mynewcity.view.UIUpdateProvider

class MainController(
    private val locationProvider: LocationProvider,
    private val gridUpdateProvider: GridUpdateProvider,
    private val uiUpdateProvider: UIUpdateProvider,
    private val executeOnUiThread: (() -> Unit) -> Unit
) {

    private var trackingOriginLat = GridConfig.ORIGIN_LAT
    private var trackingOriginLon = GridConfig.ORIGIN_LON
    private var lastLocation: LocationData? = null
    private var radiusCells = 20
    private var totalCellCount = 1
    private var allCells: Set<GridCell> = emptySet()
    private var isInitialized = false

    fun setRadius(radiusKm: Double) {
        radiusCells = GridConfig.radiusCellsFromKm(radiusKm)
    }

    fun startTracking() {

        // Ursprung und Raster nur beim allerersten Start (bzw. nach einem Reset)
        // festlegen - ein Stop/Start-Zyklus setzt die Simulation sonst am selben
        // Ursprung mit demselben Raster fort, statt neu zu beginnen
        if (!isInitialized) {
            val (mapLat, mapLon) = uiUpdateProvider.getMapCenter()
            trackingOriginLat = mapLat
            trackingOriginLon = mapLon

            locationProvider.setStartPosition(mapLat, mapLon)

            allCells = gridUpdateProvider.generateGrid(
                trackingOriginLat,
                trackingOriginLon,
                radiusCells
            )
            totalCellCount = allCells.size
            isInitialized = true

            executeOnUiThread {
                uiUpdateProvider.initializeGrid(allCells)
            }
        }

        uiUpdateProvider.updateTrackingState(true)

        locationProvider.start { location ->

            lastLocation = location

            Log.d(
                "MYNEWCITY_TEST",
                "FakeGPS: ${location.latitude}, ${location.longitude}"
            )

            val cell = gridUpdateProvider.toGridCell(location.latitude, location.longitude)

            // nur zählen, wenn die Position auch tatsächlich im Raster liegt -
            // sonst würde der Fortschritt auch außerhalb des Umkreises steigen
            val isInsideGrid = allCells.contains(cell)

            if (isInsideGrid) {
                gridUpdateProvider.addLocation(location.latitude, location.longitude)
            }

            Log.d(
                "MYNEWCITY_TEST",
                "GRID: cell=${cell.x}, ${cell.y}, insideGrid=$isInsideGrid"
            )

            val visitedCount = gridUpdateProvider.getVisitedCount()
            val progressPercent = (visitedCount * 100) / totalCellCount

            executeOnUiThread {
                uiUpdateProvider.updateLocationMarker(location.latitude, location.longitude)

                if (isInsideGrid) {
                    uiUpdateProvider.updateVisited()
                    uiUpdateProvider.updateVisitedCells(visitedCount)
                    uiUpdateProvider.updateProgress(progressPercent)
                }
            }
        }
    }

    fun stopTracking() {
        locationProvider.stop()
        uiUpdateProvider.updateTrackingState(false)
    }

    fun resetTracking() {
        locationProvider.stop()
        gridUpdateProvider.reset()
        isInitialized = false
        totalCellCount = 1
        allCells = emptySet()
        lastLocation = null

        executeOnUiThread {
            uiUpdateProvider.clearGrid()
            uiUpdateProvider.clearLocationMarker()
            uiUpdateProvider.updateTrackingState(false)
            uiUpdateProvider.updateVisitedCells(0)
            uiUpdateProvider.updateProgress(0)
        }
    }

    fun centerMap() {
        val location = lastLocation ?: return

        executeOnUiThread {
            uiUpdateProvider.centerOn(location.latitude, location.longitude)
        }
    }
}