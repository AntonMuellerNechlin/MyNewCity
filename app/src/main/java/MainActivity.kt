package com.example.mynewcity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
// Import für Logging
import android.util.Log
// Import für FakeGPS
import com.example.mynewcity.location.FakeLocationSource

class MainActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var fakeLocationSource: FakeLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        // Ab hier Fake-GPS einbindung
        fakeLocationSource = FakeLocationSource()

        fakeLocationSource.start { location ->

            Log.d(
                "FakeGPS",
                "${location.latitude}, ${location.longitude}"
            )
        }
        //
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("osmdroid", MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = packageName

        setContentView(R.layout.activity_main)

        map = findViewById(R.id.map)

        map.setMultiTouchControls(true)

        val controller = map.controller
        controller.setZoom(15.0)
        print("Test")
        val startPoint = GeoPoint(52.5200, 13.4050) // Berlin
        controller.setCenter(startPoint)
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