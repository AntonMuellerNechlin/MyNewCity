package com.example.mynewcity.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.mynewcity.model.GridCell
import com.example.mynewcity.model.GridConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

// Zeichnet das Raster direkt auf die Canvas, statt ein Polygon-Objekt pro
// Zelle zu verwalten. So werden bei jedem Zeichnen nur die Zellen berechnet,
// die im aktuellen Kartenausschnitt überhaupt sichtbar sind - unabhängig
// davon, wie groß der insgesamt eingestellte Umkreis ist.
class GridOverlay(private val map: MapView) : Overlay(), GridVisualizationProvider {

    private var visitedCells: Set<GridCell> = emptySet()
    private var hasGrid = false

    private var minX = 0
    private var maxX = 0
    private var minY = 0
    private var maxY = 0

    private val visitedPaint = Paint().apply {
        color = Color.argb(120, 0, 150, 255)
        style = Paint.Style.FILL
    }

    private val unvisitedPaint = Paint().apply {
        color = Color.argb(20, 0, 0, 0)
        style = Paint.Style.FILL
    }

    private val outlinePaint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    init {
        map.overlays.add(this)
    }

    override fun initializeGrid(allCells: Set<GridCell>) {
        minX = allCells.minOf { it.x }
        maxX = allCells.maxOf { it.x }
        minY = allCells.minOf { it.y }
        maxY = allCells.maxOf { it.y }
        visitedCells = emptySet()
        hasGrid = true
        map.postInvalidate()
    }

    override fun updateVisited(visitedCells: Set<GridCell>) {
        this.visitedCells = visitedCells
        map.postInvalidate()
    }

    override fun clearGrid() {
        hasGrid = false
        visitedCells = emptySet()
        map.postInvalidate()
    }

    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
        if (shadow || !hasGrid) return

        val originLat = GridConfig.ORIGIN_LAT
        val originLon = GridConfig.ORIGIN_LON

        val metersPerDegLat = 111320.0
        val metersPerDegLon = 111320.0 * cos(Math.toRadians(originLat))
        val cellSize = GridConfig.CELL_SIZE_METERS

        // sichtbaren Kartenausschnitt in Zellen-Indizes umrechnen
        val boundingBox = mapView.boundingBox

        val visibleMinX = floor(((boundingBox.lonWest - originLon) * metersPerDegLon) / cellSize).toInt()
        val visibleMaxX = ceil(((boundingBox.lonEast - originLon) * metersPerDegLon) / cellSize).toInt()
        val visibleMinY = floor(((boundingBox.latSouth - originLat) * metersPerDegLat) / cellSize).toInt()
        val visibleMaxY = ceil(((boundingBox.latNorth - originLat) * metersPerDegLat) / cellSize).toInt()

        // auf den tatsächlich konfigurierten Umkreis begrenzen
        val startX = max(minX, visibleMinX)
        val endX = min(maxX, visibleMaxX)
        val startY = max(minY, visibleMinY)
        val endY = min(maxY, visibleMaxY)

        if (startX > endX || startY > endY) return

        val projection = mapView.projection
        val sizeLat = cellSize / metersPerDegLat
        val sizeLon = cellSize / metersPerDegLon

        for (x in startX..endX) {
            for (y in startY..endY) {

                val lat = originLat + (y * cellSize) / metersPerDegLat
                val lon = originLon + (x * cellSize) / metersPerDegLon

                val point1 = projection.toPixels(GeoPoint(lat, lon), null)
                val point2 = projection.toPixels(GeoPoint(lat + sizeLat, lon + sizeLon), null)

                val left = min(point1.x, point2.x).toFloat()
                val right = max(point1.x, point2.x).toFloat()
                val top = min(point1.y, point2.y).toFloat()
                val bottom = max(point1.y, point2.y).toFloat()

                val isVisited = visitedCells.contains(GridCell(x, y))
                val fillPaint = if (isVisited) visitedPaint else unvisitedPaint

                canvas.drawRect(left, top, right, bottom, fillPaint)
                canvas.drawRect(left, top, right, bottom, outlinePaint)
            }
        }
    }
}
