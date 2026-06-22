package com.example.mynewcity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mynewcity.controller.MainController
import com.example.mynewcity.location.FakeLocationSource
import com.example.mynewcity.model.GridManager
import com.example.mynewcity.view.GridOverlay
import com.example.mynewcity.view.MapRenderer
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView

class MainActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var mapRenderer: MapRenderer
    private lateinit var mainController: MainController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName

        setContentView(R.layout.activity_main)

        map = findViewById(R.id.map)

        val gridOverlay = GridOverlay(map)

        mapRenderer = MapRenderer(
            map,
            gridOverlay
        )

        mapRenderer.setupMap()

        mainController = MainController(
            locationProvider = FakeLocationSource(),
            gridManager = GridManager(),
            mapRenderer = mapRenderer,
            executeOnUiThread = { action ->
                runOnUiThread(action)
            }
        )

        mainController.startTracking()
    }

    override fun onResume() {
        super.onResume()
        mapRenderer.resume()
    }

    override fun onPause() {
        super.onPause()
        mapRenderer.pause()
    }

    override fun onDestroy() {
        mainController.stopTracking()
        super.onDestroy()
    }
}