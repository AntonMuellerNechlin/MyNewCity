package com.example.mynewcity.model

interface GridUpdateProvider {

    fun addLocation(lat: Double, lon: Double): GridCell
}