package com.example.mynewcity.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.mynewcity.model.GridCell
import com.example.mynewcity.model.GridConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

class MapRenderer(
    private val map: MapView,
    private val mapDataProvider: MapDataProvider,
    private val gridVisualizationProvider: GridVisualizationProvider
) : MapViewProvider {

    private var currentLocation: GeoPoint? = null

    private val locationOuterPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val locationInnerPaint = Paint().apply {
        color = Color.rgb(30, 144, 255)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    // zeigt die aktuelle (Fake-)GPS-Position als blauen Punkt mit weißer
    // Umrandung an, ähnlich dem Standort-Punkt in Apple/Google Maps.
    // Läuft unabhängig vom Raster - die Position wird immer angezeigt,
    // auch außerhalb des konfigurierten Umkreises.
    private val locationMarkerOverlay = object : Overlay() {
        override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
            if (shadow) return

            val location = currentLocation ?: return
            val point = mapView.projection.toPixels(location, null)

            canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), LOCATION_OUTER_RADIUS_PX, locationOuterPaint)
            canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), LOCATION_INNER_RADIUS_PX, locationInnerPaint)
        }
    }

    init {
        map.overlays.add(locationMarkerOverlay)
    }

    override fun setupMap() {
        mapDataProvider.configureMap(map)
        centerMap()
    }

    fun centerMap() {
        centerOn(GridConfig.ORIGIN_LAT, GridConfig.ORIGIN_LON)
    }

    override fun centerOn(lat: Double, lon: Double) {
        map.controller.setCenter(GeoPoint(lat, lon))
    }

    override fun getMapCenter(): Pair<Double, Double> {
        val center = map.mapCenter
        return center.latitude to center.longitude
    }

    override fun initializeGrid(allCells: Set<GridCell>) {
        gridVisualizationProvider.initializeGrid(allCells)
    }

    override fun updateVisited() {
        gridVisualizationProvider.updateVisited()
    }

    override fun clearGrid() {
        gridVisualizationProvider.clearGrid()
    }

    override fun updateLocationMarker(lat: Double, lon: Double) {
        currentLocation = GeoPoint(lat, lon)
        map.postInvalidate()
    }

    override fun clearLocationMarker() {
        currentLocation = null
        map.postInvalidate()
    }

    override fun resume() {
        map.onResume()
    }

    override fun pause() {
        map.onPause()
    }

    companion object {
        private const val LOCATION_OUTER_RADIUS_PX = 12f
        private const val LOCATION_INNER_RADIUS_PX = 8f
    }
}
