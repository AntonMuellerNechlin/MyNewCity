package com.example.mynewcity.view

import org.osmdroid.views.MapView

// kapselt die osmdroid-spezifische Kartenkonfiguration, damit MapRenderer
// nicht direkt von osmdroid-Detailkonfiguration abhängen muss
class OsmMapDataProvider : MapDataProvider {

    override fun configureMap(map: MapView) {
        map.setMultiTouchControls(true)
        map.controller.setZoom(DEFAULT_ZOOM)
    }

    companion object {
        private const val DEFAULT_ZOOM = 18.0
    }
}
