package com.andrescalco.simplelauncher.core

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LauncherStateHolderTest {

    private var elapsedMillis = 0L

    private fun launcher(configuration: Configuration = Configuration.Default) =
        LauncherStateHolder(
            configuration = configuration,
            now = LocalDateTime.of(2025, 10, 25, 9, 30),
            elapsedMillis = { elapsedMillis },
        )

    private fun LauncherStateHolder.positionOf(label: String) =
        homeScreen.value.tiles.indexOfFirst { it.label == label }

    @Test
    fun `tapping the WhatsApp Tile asks to open WhatsApp`() {
        val launcher = launcher()

        val request = launcher.onTileTapped(launcher.positionOf("WhatsApp"))

        assertEquals(OutgoingRequest.OpenApp("com.whatsapp"), request)
    }

    @Test
    fun `a second tap within a second is ignored`() {
        val launcher = launcher()
        val whatsApp = launcher.positionOf("WhatsApp")

        elapsedMillis = 10_000
        val first = launcher.onTileTapped(whatsApp)
        elapsedMillis = 10_600
        val second = launcher.onTileTapped(whatsApp)

        assertEquals(OutgoingRequest.OpenApp("com.whatsapp"), first)
        assertNull(second)
    }

    @Test
    fun `a tap after the debounce window opens the app again`() {
        val launcher = launcher()
        val whatsApp = launcher.positionOf("WhatsApp")

        elapsedMillis = 10_000
        launcher.onTileTapped(whatsApp)
        elapsedMillis = 11_000
        val again = launcher.onTileTapped(whatsApp)

        assertEquals(OutgoingRequest.OpenApp("com.whatsapp"), again)
    }

    @Test
    fun `battery below 20 percent shows the charging warning`() {
        val launcher = launcher()

        launcher.onBatteryLevelChanged(19)

        assertEquals(BatteryIndicator(percent = 19, isLow = true), launcher.homeScreen.value.battery)
    }

    @Test
    fun `battery at 20 percent shows no warning`() {
        val launcher = launcher()

        launcher.onBatteryLevelChanged(20)

        assertEquals(BatteryIndicator(percent = 20, isLow = false), launcher.homeScreen.value.battery)
    }

    @Test
    fun `the date is written out in Portuguese`() {
        val launcher = launcher()

        assertEquals("Sábado, 25 de outubro", launcher.homeScreen.value.date)
    }

    @Test
    fun `the first day of the month is written as an ordinal`() {
        val launcher = launcher()

        launcher.onTimeChanged(LocalDateTime.of(2026, 3, 1, 8, 0))

        assertEquals("Domingo, 1º de março", launcher.homeScreen.value.date)
    }

    @Test
    fun `the clock shows hours and minutes in 24-hour time`() {
        val launcher = launcher()

        launcher.onTimeChanged(LocalDateTime.of(2025, 10, 25, 21, 5))

        assertEquals("21:05", launcher.homeScreen.value.time)
    }
}
