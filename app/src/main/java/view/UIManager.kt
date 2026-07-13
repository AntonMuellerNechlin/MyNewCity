package com.example.mynewcity.view

import android.app.AlertDialog
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.mynewcity.R

class UIManager(
    private val buttonToggle: Button,
    buttonReset: Button,
    buttonCenter: ImageButton,
    private val textProgress: TextView,
    onStartClicked: () -> Unit,
    onStopClicked: () -> Unit,
    onResetClicked: () -> Unit,
    onCenterClicked: () -> Unit
) : UIUpdateProvider {

    private var isRunning = false
    private var visitedCount = 0
    private var progressPercent = 0

    init {
        buttonToggle.setOnClickListener {
            if (isRunning) onStopClicked() else onStartClicked()
        }

        buttonReset.setOnClickListener {
            AlertDialog.Builder(buttonReset.context)
                .setTitle("Reset")
                .setMessage("Wirklich resetten?")
                .setPositiveButton("Ja") { _, _ -> onResetClicked() }
                .setNegativeButton("Nein", null)
                .show()
        }

        buttonCenter.setOnClickListener { onCenterClicked() }
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
        visitedCount = count
        refreshProgressText()
    }

    override fun updateProgress(percent: Int) {
        progressPercent = percent
        refreshProgressText()
    }

    private fun refreshProgressText() {
        textProgress.text = "$visitedCount Zellen besucht ($progressPercent%)"
    }
}
