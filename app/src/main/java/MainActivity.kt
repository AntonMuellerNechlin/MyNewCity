package com.example.mynewcity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mynewcity.location.FakeLocationSource
import com.example.mynewcity.location.LocationProvider
import com.example.mynewcity.model.GridConfig
import com.example.mynewcity.model.GridManager
import com.example.mynewcity.view.GridOverlay
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MainActivity : AppCompatActivity() {

    private lateinit var map: MapView

    private lateinit var locationProvider: LocationProvider
    private lateinit var gridManager: GridManager
    private lateinit var gridOverlay: GridOverlay

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        gridManager = GridManager()

        Configuration.getInstance().userAgentValue = packageName

        setContentView(R.layout.activity_main)

        map = findViewById(R.id.map)
        gridOverlay = GridOverlay(map)

        map.setMultiTouchControls(true)

        val controller = map.controller
        controller.setZoom(18.0)

        val startPoint = GeoPoint(
            GridConfig.ORIGIN_LAT,
            GridConfig.ORIGIN_LON
        )

        controller.setCenter(startPoint)

        locationProvider = FakeLocationSource()

        locationProvider.start { location ->

            Log.d(
                "MYNEWCITY_TEST",
                "FakeGPS: ${location.latitude}, ${location.longitude}"
            )

            val cell = gridManager.addLocation(
                location.latitude,
                location.longitude
            )

            Log.d(
                "MYNEWCITY_TEST",
                "GRID: cell=${cell.x}, ${cell.y}"
            )

            Log.d(
                "MYNEWCITY_TEST",
                "Visited cells: ${gridManager.getVisitedCells().size}"
            )

            runOnUiThread {

                val visited = gridManager.getVisitedCells()

                gridOverlay.setVisited(visited)

                val grid = gridManager.generateGrid(
                    GridConfig.ORIGIN_LAT,
                    GridConfig.ORIGIN_LON
                )

                gridOverlay.drawGrid(grid)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroy() {
        locationProvider.stop()
        super.onDestroy()
    }
}