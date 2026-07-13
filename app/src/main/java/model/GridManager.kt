package com.example.mynewcity.model

class GridManager : GridUpdateProvider, GridDataProvider {

    private val visitedCells = mutableSetOf<GridCell>()

    fun toGridCell(lat: Double, lon: Double): GridCell {

        val metersPerDegLat = 111320.0
        val metersPerDegLon = 111320.0 * kotlin.math.cos(Math.toRadians(lat))

        val xMeters = (lon - GridConfig.ORIGIN_LON) * metersPerDegLon
        val yMeters = (lat - GridConfig.ORIGIN_LAT) * metersPerDegLat

        val x = (xMeters / GridConfig.CELL_SIZE_METERS).toInt()
        val y = (yMeters / GridConfig.CELL_SIZE_METERS).toInt()

        return GridCell(x, y)
    }

    override fun addLocation(lat: Double, lon: Double): GridCell {
        val cell = toGridCell(lat, lon)
        visitedCells.add(cell)
        return cell
    }

    override fun getVisitedCells(): Set<GridCell> {
        return visitedCells
    }

    fun reset() {
        visitedCells.clear()
    }

    override fun generateGrid(
        centerLat: Double,
        centerLon: Double,
        radius: Int
    ): Set<GridCell> {

        val center = toGridCell(centerLat, centerLon)

        val grid = mutableSetOf<GridCell>()

        for (dx in -radius..radius) {
            for (dy in -radius..radius) {
                grid.add(
                    GridCell(center.x + dx, center.y + dy)
                )
            }
        }

        return grid
    }
}