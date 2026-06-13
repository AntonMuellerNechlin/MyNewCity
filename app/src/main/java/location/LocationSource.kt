package com.example.mynewcity.location

// Interface
// der MainController kennt später nur dieses Interface
// App kann daher mit Simulierten, als auch mit echten Daten arbeiten
interface LocationSource {

    fun start(
        onLocationUpdate: (LocationData) -> Unit
    )

    fun stop()
}