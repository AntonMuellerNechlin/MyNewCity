package com.example.mynewcity.view

import android.app.AlertDialog
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.example.mynewcity.R
import com.example.mynewcity.model.GridCell

class UIManager(
    private val buttonToggle: Button,
    private val buttonReset: Button,
    buttonCenter: ImageButton,
    private val progressContainer: View,
    private val progressBar: ProgressBar,
    private val textProgress: TextView,
    private val mapViewProvider: MapViewProvider,
    onStartClicked: () -> Unit,
    onStopClicked: () -> Unit,
    onResetClicked: () -> Unit,
    onCenterClicked: () -> Unit,
    onRadiusConfirmed: (Double) -> Unit
) : UIUpdateProvider {

    private var isRunning = false
    private var needsRadiusPrompt = true

    init {
        buttonToggle.setOnClickListener {
            when {
                isRunning -> onStopClicked()
                needsRadiusPrompt -> showRadiusDialog { radiusKm ->
                    needsRadiusPrompt = false
                    progressContainer.visibility = View.VISIBLE
                    buttonReset.visibility = View.VISIBLE
                    onRadiusConfirmed(radiusKm)
                }
                else -> onStartClicked()
            }
        }

        buttonReset.setOnClickListener {
            showStyledDialog(
                AlertDialog.Builder(buttonReset.context)
                    .setTitle("Reset")
                    .setMessage("Wirklich resetten? Das Tracking wird gestoppt und das gesamte Raster gelöscht. Du musst danach einen neuen Umkreis festlegen.")
                    .setPositiveButton("Ja") { _, _ ->
                        needsRadiusPrompt = true
                        progressContainer.visibility = View.GONE
                        buttonReset.visibility = View.GONE
                        onResetClicked()
                    }
                    .setNegativeButton("Nein", null)
            )
        }

        buttonCenter.setOnClickListener { onCenterClicked() }
    }

    fun onResume() {
        mapViewProvider.resume()
    }

    fun onPause() {
        mapViewProvider.pause()
    }

    private fun showRadiusDialog(onConfirmed: (Double) -> Unit) {
        val context = buttonToggle.context

        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        input.hint = "z.B. 1.5"

        showStyledDialog(
            AlertDialog.Builder(context)
                .setTitle("Umkreis festlegen")
                .setMessage("Wie viele Kilometer soll das Raster umfassen? (max. $MAX_RADIUS_KM km)")
                .setView(input)
                .setPositiveButton("OK") { _, _ ->
                    val text = input.text.toString()

                    // leeres Feld -> stiller Default, erleichtert das Testen
                    val radiusKm = if (text.isBlank()) DEFAULT_RADIUS_KM else text.toDoubleOrNull()

                    if (radiusKm == null || radiusKm <= 0.0 || radiusKm > MAX_RADIUS_KM) {
                        showRadiusError(onConfirmed)
                    } else {
                        onConfirmed(radiusKm)
                    }
                }
                .setNegativeButton("Abbrechen", null)
        )
    }

    private fun showRadiusError(onConfirmed: (Double) -> Unit) {
        showStyledDialog(
            AlertDialog.Builder(buttonToggle.context)
                .setTitle("Ungültiger Umkreis")
                .setMessage("Bitte gib einen Umkreis zwischen 0 und $MAX_RADIUS_KM km an.")
                .setPositiveButton("OK") { _, _ -> showRadiusDialog(onConfirmed) }
        )
    }

    // gemeinsames, abgerundetes Erscheinungsbild für alle Dialoge dieser App
    private fun showStyledDialog(builder: AlertDialog.Builder) {
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_rounded)
        dialog.show()
    }

    override fun updateTrackingState(isRunning: Boolean) {
        this.isRunning = isRunning

        if (isRunning) {
            buttonToggle.text = "Stop"
            buttonToggle.setBackgroundResource(R.drawable.bg_button_round_red)
        } else {
            buttonToggle.text = "Start"
            buttonToggle.setBackgroundResource(R.drawable.bg_button_round_blue)
        }
    }

    override fun updateVisitedCells(count: Int) {
        // Zellenzahl wird aktuell nicht separat angezeigt, der Balken +
        // die Prozentzahl in updateProgress() reichen als Anzeige aus
    }

    override fun updateProgress(percent: Int) {
        progressBar.progress = percent
        textProgress.text = "$percent%"
    }

    // reine Weiterleitung an MapRenderer - MainController kennt MapViewProvider
    // laut Komponentendiagramm nicht direkt, sondern nur über UIUpdateProvider
    override fun getMapCenter(): Pair<Double, Double> {
        return mapViewProvider.getMapCenter()
    }

    override fun centerOn(lat: Double, lon: Double) {
        mapViewProvider.centerOn(lat, lon)
    }

    override fun initializeGrid(allCells: Set<GridCell>) {
        mapViewProvider.initializeGrid(allCells)
    }

    override fun updateVisited() {
        mapViewProvider.updateVisited()
    }

    override fun clearGrid() {
        mapViewProvider.clearGrid()
    }

    override fun updateLocationMarker(lat: Double, lon: Double) {
        mapViewProvider.updateLocationMarker(lat, lon)
    }

    override fun clearLocationMarker() {
        mapViewProvider.clearLocationMarker()
    }

    companion object {
        private const val DEFAULT_RADIUS_KM = 1.0
        private const val MAX_RADIUS_KM = 15.0
    }
}
