package com.andrescalco.simplelauncher.core

import java.time.LocalDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeScreenState(
    val time: String,
    val date: String,
    val tiles: List<Tile>,
    /** Null until the phone first reports its battery level. */
    val battery: BatteryIndicator? = null,
)

/** [isLow] means the Home Screen turns the indicator red and asks the Owner to charge. */
data class BatteryIndicator(val percent: Int, val isLow: Boolean)

/**
 * Decides what the Launcher shows and where the Owner's taps lead. Plain Kotlin, no Android:
 * device facts and taps come in, screen state and [OutgoingRequest]s go out.
 */
class LauncherStateHolder(
    configuration: Configuration,
    now: LocalDateTime,
    private val elapsedMillis: () -> Long,
) {
    private val _homeScreen = MutableStateFlow(
        HomeScreenState(time = ptBrTime(now), date = ptBrDate(now), tiles = configuration.tiles),
    )
    val homeScreen: StateFlow<HomeScreenState> = _homeScreen.asStateFlow()

    fun onTimeChanged(now: LocalDateTime) {
        _homeScreen.update { it.copy(time = ptBrTime(now), date = ptBrDate(now)) }
    }

    fun onBatteryLevelChanged(percent: Int) {
        _homeScreen.update {
            it.copy(battery = BatteryIndicator(percent, isLow = percent < LOW_BATTERY_PERCENT))
        }
    }

    private var lastAcceptedTapMillis: Long? = null

    /**
     * Returns where the tap leads, or null if the Tile does nothing yet or the tap repeats one
     * accepted less than [TAP_DEBOUNCE_MILLIS] ago.
     */
    fun onTileTapped(position: Int): OutgoingRequest? {
        val tapMillis = elapsedMillis()
        val last = lastAcceptedTapMillis
        if (last != null && tapMillis - last < TAP_DEBOUNCE_MILLIS) return null
        lastAcceptedTapMillis = tapMillis

        return when (val kind = _homeScreen.value.tiles[position].kind) {
            is TileKind.App -> OutgoingRequest.OpenApp(kind.packageName)
            else -> null
        }
    }

    companion object {
        const val TAP_DEBOUNCE_MILLIS = 1_000L
        const val LOW_BATTERY_PERCENT = 20
    }
}
