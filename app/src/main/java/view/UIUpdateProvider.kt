package com.example.mynewcity.view

interface UIUpdateProvider {

    fun updateTrackingState(isRunning: Boolean)

    fun updateVisitedCells(count: Int)

    fun updateProgress(percent: Int)
}