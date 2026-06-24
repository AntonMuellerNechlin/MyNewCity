package com.example.mynewcity.view

import com.example.mynewcity.model.GridCell

interface GridVisualizationProvider {

    fun setVisited(visitedCells: Set<GridCell>)

    fun drawGrid(allCells: Set<GridCell>)
}