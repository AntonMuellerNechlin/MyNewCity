package com.example.mynewcity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun renderMapTest() {
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }

    // "clickButtonTest": Klick simulieren und prüfen, ob die
    // Simulation danach als laufend markiert wird (Start/Stop-Umschaltung)
    @Test
    fun clickButtonTest() {
        onView(withId(R.id.buttonToggle))
            .perform(click())

        // beim ersten Start erscheint der Umkreis-Dialog, leeres Feld ->
        // stiller Standardradius nach Bestätigung mit "OK"
        onView(withText("OK"))
            .inRoot(isDialog())
            .perform(click())

        onView(withId(R.id.buttonToggle))
            .check(matches(withText("Stop")))
    }

    // "changeUiStateTest": UI-Zustand nach Aktion prüfen
    @Test
    fun changeUiStateTest() {
        onView(withId(R.id.progressContainer))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.buttonToggle))
            .perform(click())

        onView(withText("OK"))
            .inRoot(isDialog())
            .perform(click())

        // nach Start: Fortschrittsanzeige sichtbar, Button zeigt "Stop"
        onView(withId(R.id.progressContainer))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.buttonToggle))
            .check(matches(withText("Stop")))

        onView(withId(R.id.buttonToggle))
            .perform(click())

        // nach Stop: Button zeigt wieder "Start"
        onView(withId(R.id.buttonToggle))
            .check(matches(withText("Start")))
    }
}
