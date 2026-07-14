package com.example.mynewcity.view

import com.example.mynewcity.model.GridCell

interface GridVisualizationProvider {

    // legt fest, welcher Bereich (in Zellen) überhaupt zum Raster gehört
    fun initializeGrid(allCells: Set<GridCell>)

    // signalisiert, dass sich die besuchten Zellen geändert haben - die
    // aktuellen Daten werden dabei selbst über GridDataProvider abgeholt
    fun updateVisited()

    // entfernt das Raster komplett (z.B. bei Reset)
    fun clearGrid()
}