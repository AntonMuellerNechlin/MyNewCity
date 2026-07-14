package com.example.mynewcity.model

class GridManager : GridUpdateProvider, GridDataProvider {

    private val visitedCells = mutableSetOf<GridCell>()

    override fun toGridCell(lat: Double, lon: Double): GridCell {

        val metersPerDegLat = 111320.0

        // WICHTIG: cos() der festen Ursprungsbreite (nicht der aktuellen
        // lat) verwenden - GridOverlay.draw() zeichnet die Zellen ebenfalls
        // mit cos(ORIGIN_LAT). Beide Stellen müssen exakt dieselbe Skala
        // benutzen, sonst driftet die Meter-pro-Längengrad-Umrechnung mit
        // wachsender Entfernung vom Ursprung auseinander (Breite UND Länge),
        // und das Raster verschiebt sich dadurch um mehrere Kilometer.
        val metersPerDegLon = 111320.0 * kotlin.math.cos(Math.toRadians(GridConfig.ORIGIN_LAT))

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