package com.example.mynewcity.model

class GridManager {

    private val visitedCells = mutableSetOf<GridCell>()

    fun addPosition(lat: Double, lon: Double): GridCell {

        val x = (lon * 1000).toInt()
        val y = (lat * 1000).toInt()

        val cell = GridCell(x, y)

        visitedCells.add(cell)

        return cell
    }

    fun getVisitedCells(): Set<GridCell> {
        return visitedCells
    }
}