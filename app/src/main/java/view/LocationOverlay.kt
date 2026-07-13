package com.example.mynewcity.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

// zeigt die aktuelle (Fake-)GPS-Position als blauen Punkt mit weißer
// Umrandung an, ähnlich dem Standort-Punkt in Apple/Google Maps.
// Läuft unabhängig vom Raster - die Position wird immer angezeigt,
// auch außerhalb des konfigurierten Umkreises.
class LocationOverlay(private val map: MapView) : Overlay() {

    private var currentLocation: GeoPoint? = null

    private val outerPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val innerPaint = Paint().apply {
        color = Color.rgb(30, 144, 255)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    init {
        map.overlays.add(this)
    }

    fun updateLocation(lat: Double, lon: Double) {
        currentLocation = GeoPoint(lat, lon)
        map.postInvalidate()
    }

    fun clearLocation() {
        currentLocation = null
        map.postInvalidate()
    }

    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
        if (shadow) return

        val location = currentLocation ?: return
        val point = mapView.projection.toPixels(location, null)

        canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), OUTER_RADIUS_PX, outerPaint)
        canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), INNER_RADIUS_PX, innerPaint)
    }

    companion object {
        private const val OUTER_RADIUS_PX = 12f
        private const val INNER_RADIUS_PX = 8f
    }
}
