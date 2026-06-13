package com.example.mynewcity.view

import android.graphics.Color
import android.graphics.Paint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon
import com.example.mynewcity.model.GridCell

class GridOverlay(private val map: MapView) {

    fun drawCells(cells: Set<GridCell>) {

        // erstmal clearen am Anfang (später ersetzen)
        map.overlays.clear()

        for (cell in cells) {

            val lat = cell.y / 10000.0
            val lon = cell.x / 10000.0

            // Größe einer Grid-Zelle
            val size = 0.00005

            val polygon = Polygon(map)

            val points = listOf(
                GeoPoint(lat, lon),
                GeoPoint(lat + size, lon),
                GeoPoint(lat + size, lon + size),
                GeoPoint(lat, lon + size)
            )

            polygon.points = points

            val fillPaint = Paint().apply {
                style = Paint.Style.FILL
                color = Color.argb(80, 0, 150, 255)
            }

            val strokePaint = Paint().apply {
                style = Paint.Style.STROKE
                color = Color.BLUE
                strokeWidth = 2f
            }

            polygon.fillPaint = fillPaint
            polygon.outlinePaint = strokePaint

            map.overlays.add(polygon)
        }

        map.invalidate()
    }
}