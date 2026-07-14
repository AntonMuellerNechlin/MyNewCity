package com.example.mynewcity.view

import org.osmdroid.views.MapView

// stellt dem MapRenderer die Kartenkacheln/-konfiguration von OpenStreetMap
// bereit (siehe Whitepaper Kapitel 7.2/10). Kapselt die osmdroid-spezifische
// Einrichtung, damit MapRenderer nicht direkt von osmdroid-Detailkonfiguration
// abhängen muss.
class OsmMapDataProvider : MapDataProvider {

    override fun configureMap(map: MapView) {
        map.setMultiTouchControls(true)
        map.controller.setZoom(DEFAULT_ZOOM)
    }

    companion object {
        private const val DEFAULT_ZOOM = 18.0
    }
}
