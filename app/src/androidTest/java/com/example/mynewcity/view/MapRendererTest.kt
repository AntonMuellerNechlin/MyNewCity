package com.example.mynewcity.view

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.osmdroid.views.MapView

// Whitepaper 7.3 "centerMapTest": feste Koordinate setzen und prüfen,
// ob die Kartenansicht danach mittig auf dieser Koordinate steht
@RunWith(AndroidJUnit4::class)
class MapRendererTest {

    @Test
    fun centerMapTest() {
        val targetLat = 48.1351
        val targetLon = 11.5820

        var resultLat = 0.0
        var resultLon = 0.0

        // MapView erzeugt intern einen Handler und braucht deshalb einen
        // vorbereiteten Looper - deshalb läuft dieser Teil auf dem Main-Thread
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val context = ApplicationProvider.getApplicationContext<android.content.Context>()

            val map = MapView(context)
            map.measure(MAP_SIZE_PX, MAP_SIZE_PX)
            map.layout(0, 0, MAP_SIZE_PX, MAP_SIZE_PX)

            val mapRenderer = MapRenderer(
                map,
                OsmMapDataProvider(),
                GridOverlay(map),
                LocationOverlay(map)
            )

            // Zoomstufe muss gesetzt sein, bevor die Projektion sinnvolle
            // Pixel-/Geo-Umrechnungen liefert - setupMap() erledigt das
            mapRenderer.setupMap()
            mapRenderer.centerOn(targetLat, targetLon)

            val (lat, lon) = mapRenderer.getMapCenter()
            resultLat = lat
            resultLon = lon
        }

        assertEquals(targetLat, resultLat, 0.001)
        assertEquals(targetLon, resultLon, 0.001)
    }

    companion object {
        private const val MAP_SIZE_PX = 800
    }
}
