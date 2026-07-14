package com.example.mynewcity.model

interface GridDataProvider {

    fun getVisitedCells(): Set<GridCell>
}