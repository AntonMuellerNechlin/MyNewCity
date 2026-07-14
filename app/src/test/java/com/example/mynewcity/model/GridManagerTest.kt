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

        val cell = gridManager.toGridCell(
            GridConfig.ORIGIN_LAT,
            GridConfig.ORIGIN_LON
        )

        assertEquals(GridCell(0, 0), cell)
    }

    // Regressionstest: südlich/westlich des festen Ursprungs sind die
    // Meter-Offsets negativ. toGridCell() muss dabei abrunden (floor),
    // nicht Richtung Null abschneiden - sonst landet der Standort optisch
    // eine Zelle über dem tatsächlich markierten Kästchen.
    @Test
    fun toGridCellSouthOfOriginTest() {
        val gridManager = GridManager()

        val metersPerDegLat = 111320.0
        val latSlightlySouth = GridConfig.ORIGIN_LAT - (50.0 / metersPerDegLat)

        val cell = gridManager.toGridCell(latSlightlySouth, GridConfig.ORIGIN_LON)

        assertEquals(GridCell(0, -1), cell)
    }

    // Regressionstest: für die Längengrad-Skalierung (Meter pro Grad) muss
    // immer cos(ORIGIN_LAT) verwendet werden, nicht cos(der aktuellen lat).
    // GridOverlay zeichnet Zellen ebenfalls mit cos(ORIGIN_LAT) - wird an
    // dieser Stelle stattdessen die Breite des jeweiligen Standorts benutzt,
    // driftet die berechnete Zelle bei weit entfernten Standorten (andere
    // Stadt) um mehrere hundert Meter bis Kilometer gegenüber der Zeichnung.
    @Test
    fun toGridCellFarFromOriginTest() {
        val gridManager = GridManager()

        // München: deutlich südlich und leicht westlich von Berlin
        val munichLat = 48.1351
        val munichLon = 11.5820

        val metersPerDegLat = 111320.0
        val metersPerDegLonAtOrigin =
            111320.0 * kotlin.math.cos(Math.toRadians(GridConfig.ORIGIN_LAT))

        val expectedX = kotlin.math.floor(
            ((munichLon - GridConfig.ORIGIN_LON) * metersPerDegLonAtOrigin) /
                GridConfig.CELL_SIZE_METERS
        ).toInt()
        val expectedY = kotlin.math.floor(
            ((munichLat - GridConfig.ORIGIN_LAT) * metersPerDegLat) /
                GridConfig.CELL_SIZE_METERS
        ).toInt()

        val cell = gridManager.toGridCell(munichLat, munichLon)

        assertEquals(GridCell(expectedX, expectedY), cell)
    }
}