package com.example.mynewcity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
// Import für OpenStreetMap
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
// Import für Logging
import android.util.Log
// Import für Location
import com.example.mynewcity.location.FakeLocationSource
// Import für Model
import com.example.mynewcity.model.GridManager
// Import für View
import com.example.mynewcity.view.GridOverlay


class MainActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var fakeLocationSource: FakeLocationSource
    private lateinit var gridManager: GridManager
    private lateinit var gridOverlay: GridOverlay

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        gridManager = GridManager()

        Configuration.getInstance().userAgentValue = packageName

        setContentView(R.layout.activity_main)

        // 1. Map zuerst
        map = findViewById(R.id.map)
        gridOverlay = GridOverlay(map)

        map.setMultiTouchControls(true)

        val controller = map.controller
        controller.setZoom(15.0)

        val startPoint = GeoPoint(52.5200, 13.4050)
        controller.setCenter(startPoint)
        controller.setZoom(18.0)

        // 2. Fake GPS initialisieren
        fakeLocationSource = FakeLocationSource()

        // 3. GPS Start
        fakeLocationSource.start { location ->

            Log.d("FakeGPS", "${location.latitude}, ${location.longitude}")

            val cell = gridManager.addLocation(
                location.latitude,
                location.longitude
            )

            Log.d("GRID", "cell=${cell.x}, ${cell.y}")

            runOnUiThread {

                val visited = gridManager.getVisitedCells()

                gridOverlay.setVisited(visited)

                val grid = gridManager.generateGrid(
                    52.5200,
                    13.4050
                )

                gridOverlay.drawGrid(grid)
            }
        }
    }

    private fun addMarker(lat: Double, lon: Double) {

        val point = GeoPoint(lat, lon)

        val marker = Marker(map)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Visited"

        map.overlays.add(marker)
        map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}