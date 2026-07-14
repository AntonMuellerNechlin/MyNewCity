package com.example.mynewcity.model

class GridManager : GridUpdateProvider, GridDataProvider {

    private val visitedCells = mutableSetOf<GridCell>()

    override fun toGridCell(lat: Double, lon: Double): GridCell {

        val metersPerDegLat = 111320.0
        val metersPerDegLon = 111320.0 * kotlin.math.cos(Math.toRadians(lat))

        val xMeters = (lon - GridConfig.ORIGIN_LON) * metersPerDegLon
        val yMeters = (lat - GridConfig.ORIGIN_LAT) * metersPerDegLat

        // floor() statt toInt(): toInt() rundet Richtung Null, nicht ab.
        // Bei Standorten südlich/westlich des festen Ursprungs (ORIGIN_LAT/LON
        // liegt in Berlin) sind xMeters/yMeters negativ - dort würde toInt()
        // die falsche Zelle liefern (eine Position zu weit Richtung Ursprung
        // verschoben), das Raster würde dann nicht mit dem echten Standort
        // übereinstimmen.
        val x = kotlin.math.floor(xMeters / GridConfig.CELL_SIZE_METERS).toInt()
        val y = kotlin.math.floor(yMeters / GridConfig.CELL_SIZE_METERS).toInt()

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

    override fun reset() {
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