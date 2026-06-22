package com.example.mynewcity.location

// der MainController kennt später nur dieses Interface
// App kann daher mit Simulierten, als auch mit echten Daten arbeiten
interface LocationProvider {

    fun start(
        onLocationUpdate: (LocationData) -> Unit
    )

    fun stop()
}