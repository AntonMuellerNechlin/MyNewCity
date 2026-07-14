package com.example.mynewcity.model

class GridManager : GridUpdateProvider, GridDataProvider {

    private val visitedCells = mutableSetOf<GridCell>()

    override fun toGridCell(lat: Double, lon: Double): GridCell {

        val metersPerDegLat = 111320.0

        // cos() der festen Ursprungsbreite verwenden, nicht der aktuellen lat -
        // GridOverlay.draw() zeichnet mit derselben festen Skala. Sonst driftet
        // die Zelle bei wachsender Entfernung vom Ursprung um mehrere Kilometer.
        val metersPerDegLon = 111320.0 * kotlin.math.cos(Math.toRadians(GridConfig.ORIGIN_LAT))

        val xMeters = (lon - GridConfig.ORIGIN_LON) * metersPerDegLon
        val yMeters = (lat - GridConfig.ORIGIN_LAT) * metersPerDegLat

        // floor() statt toInt(): toInt() rundet Richtung Null statt ab, das
        // liefert bei negativen Werten (südlich/westlich des Ursprungs) die
        // falsche Zelle.
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

    override fun getVisitedCount(): Int {
        return visitedCells.size
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