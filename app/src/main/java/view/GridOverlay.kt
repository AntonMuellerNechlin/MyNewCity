package com.example.mynewcity.view

import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.example.mynewcity.model.GridCell
import com.example.mynewcity.model.GridConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon


class GridOverlay(private val map: MapView) {

    private val visited = mutableSetOf<GridCell>()

    fun setVisited(cells: Set<GridCell>) {
        visited.clear()
        visited.addAll(cells)
    }

    fun drawGrid(allCells: Set<GridCell>) {

        map.overlays.removeIf { it is Polygon }

        val origin = GridConfig.ORIGIN

        val metersPerDegLat = 111320.0
        val lat0 = origin.latitude

        val metersPerDegLon0 = 111320.0 * kotlin.math.cos(Math.toRadians(lat0))

        for (cell in allCells) {

            val xMeters = cell.x * GridConfig.CELL_SIZE_METERS
            val yMeters = cell.y * GridConfig.CELL_SIZE_METERS

            val lat = origin.latitude + (yMeters / metersPerDegLat)
            val lon = origin.longitude + (xMeters / metersPerDegLon0)

            val sizeLat = GridConfig.CELL_SIZE_METERS / metersPerDegLat
            val sizeLon = GridConfig.CELL_SIZE_METERS / metersPerDegLon0

            val polygon = Polygon(map)

            polygon.points = listOf(
                GeoPoint(lat, lon),
                GeoPoint(lat + sizeLat, lon),
                GeoPoint(lat + sizeLat, lon + sizeLon),
                GeoPoint(lat, lon + sizeLon)
            )

            val isVisited = visited.contains(cell)

            polygon.fillPaint.color = if (isVisited)
                android.graphics.Color.argb(120, 0, 150, 255)
            else
                android.graphics.Color.argb(20, 0, 0, 0)

            polygon.fillPaint.style = android.graphics.Paint.Style.FILL
            polygon.outlinePaint.strokeWidth = 1f

            map.overlays.add(polygon)
        }

        map.invalidate()
    }
}