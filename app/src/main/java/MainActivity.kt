package com.example.mynewcity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mynewcity.controller.MainController
import com.example.mynewcity.location.FakeLocationSource
import com.example.mynewcity.model.GridManager
import com.example.mynewcity.view.GridOverlay
import com.example.mynewcity.view.LocationOverlay
import com.example.mynewcity.view.MapRenderer
import com.example.mynewcity.view.OsmMapDataProvider
import com.example.mynewcity.view.UIManager
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView

class MainActivity : AppCompatActivity() {

    private lateinit var uiManager: UIManager
    private lateinit var mainController: MainController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName

        setContentView(R.layout.activity_main)

        val map: MapView = findViewById(R.id.map)

        val gridOverlay = GridOverlay(map)
        val locationOverlay = LocationOverlay(map)

        val mapRenderer = MapRenderer(
            map,
            OsmMapDataProvider(),
            gridOverlay,
            locationOverlay
        )

        mapRenderer.setupMap()

        val gridManager = GridManager()

        uiManager = UIManager(
            buttonToggle = findViewById(R.id.buttonToggle),
            buttonReset = findViewById(R.id.buttonReset),
            buttonCenter = findViewById(R.id.buttonCenter),
            progressContainer = findViewById(R.id.progressContainer),
            progressBar = findViewById(R.id.progressBar),
            textProgress = findViewById(R.id.textProgress),
            mapViewProvider = mapRenderer,
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
            gridUpdateProvider = gridManager,
            gridDataProvider = gridManager,
            mapViewProvider = mapRenderer,
            uiUpdateProvider = uiManager,
            executeOnUiThread = { action ->
                runOnUiThread(action)
            }
        )
    }

    override fun onResume() {
        super.onResume()
        uiManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        uiManager.onPause()
    }

    override fun onDestroy() {
        mainController.stopTracking()
        super.onDestroy()
    }
}
