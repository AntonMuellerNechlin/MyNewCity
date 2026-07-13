package com.example.mynewcity.view

import com.example.mynewcity.model.GridCell

interface GridVisualizationProvider {

    // legt fest, welcher Bereich (in Zellen) überhaupt zum Raster gehört
    fun initializeGrid(allCells: Set<GridCell>)

    // aktualisiert, welche Zellen besucht sind (nur eine Referenz, kein Neuaufbau)
    fun updateVisited(visitedCells: Set<GridCell>)

    // entfernt das Raster komplett (z.B. bei Reset)
    fun clearGrid()
}