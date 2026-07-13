package com.example.mynewcity.location

// der MainController kennt später nur dieses Interface
// App kann daher mit Simulierten, als auch mit echten Daten arbeiten
interface LocationProvider {

    // legt fest, wo eine simulierte Quelle starten soll
    // bei einer echten GPS-Quelle wird das ignoriert
    fun setStartPosition(lat: Double, lon: Double)

    fun start(
        onLocationUpdate: (LocationData) -> Unit
    )

    fun stop()
}