package com.example.mynewcity.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GridManagerTest {

    @Test
    fun generateGridTest() {
        val gridManager = GridManager()

        val grid = gridManager.generateGrid(
            GridConfig.ORIGIN_LAT,
            GridConfig.ORIGIN_LON,
            1
        )

        assertEquals(9, grid.size)
        assertTrue(grid.contains(GridCell(0, 0)))
        assertTrue(grid.contains(GridCell(-1, -1)))
        assertTrue(grid.contains(GridCell(1, 1)))
    }

    @Test
    fun validateGridTest() {
        val gridManager = GridManager()

        val originCell = gridManager.toGridCell(
            GridConfig.ORIGIN_LAT,
            GridConfig.ORIGIN_LON
        )

        assertEquals(GridCell(0, 0), originCell)

        val visitedCell = gridManager.addLocation(
            GridConfig.ORIGIN_LAT,
            GridConfig.ORIGIN_LON
        )

        assertTrue(gridManager.getVisitedCells().contains(visitedCell))
        assertEquals(1, gridManager.getVisitedCells().size)

        gridManager.addLocation(
            GridConfig.ORIGIN_LAT,
            GridConfig.ORIGIN_LON
        )

        assertEquals(1, gridManager.getVisitedCells().size)
    }
}