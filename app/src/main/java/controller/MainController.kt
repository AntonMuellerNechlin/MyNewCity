package com.example.mynewcity.controller

import android.util.Log
import com.example.mynewcity.location.LocationData
import com.example.mynewcity.location.LocationProvider
import com.example.mynewcity.model.GridCell
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

    private var trackingOriginLat = GridConfig.ORIGIN_LAT
    private var trackingOriginLon = GridConfig.ORIGIN_LON
    private var lastLocation: LocationData? = null
    private var radiusCells = 20
    private var totalCellCount = 1
    private var allCells: Set<GridCell> = emptySet()

    fun setRadius(radiusKm: Double) {
        radiusCells = GridConfig.radiusCellsFromKm(radiusKm)
    }

    fun startTracking() {
        val (mapLat, mapLon) = mapRenderer.getMapCenter()
        trackingOriginLat = mapLat
        trackingOriginLon = mapLon

        locationProvider.setStartPosition(mapLat, mapLon)
        uiUpdateProvider.updateTrackingState(true)

        // Raster nur EINMAL pro Start aufbauen, nicht bei jedem GPS-Tick
        allCells = gridManager.generateGrid(
            trackingOriginLat,
            trackingOriginLon,
            radiusCells
        )
        totalCellCount = allCells.size

        executeOnUiThread {
            mapRenderer.initializeGrid(allCells)
        }

        locationProvider.start { location ->

            lastLocation = location

            Log.d(
                "MYNEWCITY_TEST",
                "FakeGPS: ${location.latitude}, ${location.longitude}"
            )

            val cell = gridManager.toGridCell(location.latitude, location.longitude)

            // nur zählen, wenn die Position auch tatsächlich im Raster liegt -
            // sonst würde der Fortschritt auch außerhalb des Umkreises steigen
            val isInsideGrid = allCells.contains(cell)

            if (isInsideGrid) {
                gridManager.addLocation(location.latitude, location.longitude)
            }

            Log.d(
                "MYNEWCITY_TEST",
                "GRID: cell=${cell.x}, ${cell.y}, insideGrid=$isInsideGrid"
            )

            val visitedCount = gridManager.getVisitedCells().size
            val progressPercent = (visitedCount * 100) / totalCellCount

            executeOnUiThread {
                mapRenderer.updateLocationMarker(location.latitude, location.longitude)

                if (isInsideGrid) {
                    mapRenderer.updateVisited(gridManager.getVisitedCells())
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
        gridManager.reset()
        totalCellCount = 1
        allCells = emptySet()
        lastLocation = null

        executeOnUiThread {
            mapRenderer.clearGrid()
            mapRenderer.clearLocationMarker()
            uiUpdateProvider.updateTrackingState(false)
            uiUpdateProvider.updateVisitedCells(0)
            uiUpdateProvider.updateProgress(0)
        }
    }

    fun centerMap() {
        val location = lastLocation ?: return

        executeOnUiThread {
            mapRenderer.centerOn(location.latitude, location.longitude)
        }
    }
}