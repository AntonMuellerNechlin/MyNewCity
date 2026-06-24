package com.example.mynewcity.view

import org.osmdroid.views.MapView

interface MapDataProvider {

    fun configureMap(map: MapView)
}