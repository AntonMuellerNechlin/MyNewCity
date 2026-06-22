package com.example.mynewcity.view

import android.graphics.Color
import android.graphics.Paint
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

        val originLat = GridConfig.ORIGIN_LAT
        val originLon = GridConfig.ORIGIN_LON

        val metersPerDegLat = 111320.0
        val metersPerDegLon = 111320.0 * kotlin.math.cos(Math.toRadians(originLat))

        val sizeLat = GridConfig.CELL_SIZE_METERS / metersPerDegLat
        val sizeLon = GridConfig.CELL_SIZE_METERS / metersPerDegLon

        for (cell in allCells) {

            val xMeters = cell.x * GridConfig.CELL_SIZE_METERS
            val yMeters = cell.y * GridConfig.CELL_SIZE_METERS

            val lat = originLat + (yMeters / metersPerDegLat)
            val lon = originLon + (xMeters / metersPerDegLon)

            val polygon = Polygon(map)

            polygon.points = listOf(
                GeoPoint(lat, lon),
                GeoPoint(lat + sizeLat, lon),
                GeoPoint(lat + sizeLat, lon + sizeLon),
                GeoPoint(lat, lon + sizeLon)
            )

            val isVisited = visited.contains(cell)

            if (isVisited) {
                polygon.fillPaint.color = Color.argb(120, 0, 150, 255)
            } else {
                polygon.fillPaint.color = Color.argb(20, 0, 0, 0)
            }

            polygon.fillPaint.style = Paint.Style.FILL

            polygon.outlinePaint.color = Color.GRAY
            polygon.outlinePaint.style = Paint.Style.STROKE
            polygon.outlinePaint.strokeWidth = 5f

            map.overlays.add(polygon)
        }

        map.post {
            map.invalidate()
        }
    }
}