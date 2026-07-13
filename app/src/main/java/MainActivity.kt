package com.example.mynewcity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mynewcity.controller.MainController
import com.example.mynewcity.location.FakeLocationSource
import com.example.mynewcity.model.GridManager
import com.example.mynewcity.view.GridOverlay
import com.example.mynewcity.view.LocationOverlay
import com.example.mynewcity.view.MapRenderer
import com.example.mynewcity.view.UIManager
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
        val locationOverlay = LocationOverlay(map)

        mapRenderer = MapRenderer(
            map,
            gridOverlay,
            locationOverlay
        )

        mapRenderer.setupMap()

        val uiManager = UIManager(
            buttonToggle = findViewById(R.id.buttonToggle),
            buttonReset = findViewById(R.id.buttonReset),
            buttonCenter = findViewById(R.id.buttonCenter),
            progressContainer = findViewById(R.id.progressContainer),
            progressBar = findViewById(R.id.progressBar),
            textProgress = findViewById(R.id.textProgress),
            onStartClicked = { mainController.startTracking() },
            onStopClicked = { mainController.stopTracking() },
            onResetClicked = { mainController.resetTracking() },
            onCenterClicked = { mainController.centerMap() },
            onRadiusConfirmed = { radiusKm ->
                mainController.setRadius(radiusKm)
                mainController.startTracking()
            }
        )

        mainController = MainController(
            locationProvider = FakeLocationSource(),
            gridManager = GridManager(),
            mapRenderer = mapRenderer,
            uiUpdateProvider = uiManager,
            executeOnUiThread = { action ->
                runOnUiThread(action)
            }
        )
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