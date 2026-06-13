package com.example.mynewcity.model

class GridManager {

    private val visitedCells = mutableSetOf<GridCell>()

    fun toGridCell(lat: Double, lon: Double): GridCell {

        val x = (lon * 10000).toInt()
        val y = (lat * 10000).toInt()

        return GridCell(x, y)
    }

    fun addLocation(lat: Double, lon: Double): GridCell {

        val cell = toGridCell(lat, lon)

        visitedCells.add(cell)

        return cell
    }

    fun getVisitedCells(): Set<GridCell> {
        return visitedCells
    }
}