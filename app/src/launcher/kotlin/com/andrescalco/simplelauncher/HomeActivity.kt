package com.andrescalco.simplelauncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import com.andrescalco.simplelauncher.core.LauncherStateHolder
import java.time.LocalDateTime

/** The Home Screen: the single page the Owner always returns to when pressing Home. */
class HomeActivity : ComponentActivity() {

    private lateinit var launcher: LauncherStateHolder

    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSystemService(VibratorManager::class.java).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Vibrator::class.java)
        }
    }

    private val deviceFacts = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_BATTERY_CHANGED -> batteryPercent(intent)?.let(launcher::onBatteryLevelChanged)
                else -> launcher.onTimeChanged(LocalDateTime.now())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher = LauncherStateHolder(
            configuration = ConfigurationFile(filesDir).load(),
            now = LocalDateTime.now(),
            elapsedMillis = SystemClock::elapsedRealtime,
        )

        // Same light look day and night, so the status bar icons always stay dark.
        val lightBars = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        enableEdgeToEdge(statusBarStyle = lightBars, navigationBarStyle = lightBars)

        // There is nothing behind the Home Screen to go back to.
        onBackPressedDispatcher.addCallback(this) {}

        setContent {
            SimpleTheme {
                val state by launcher.homeScreen.collectAsState()
                HomeScreen(state = state, onTileTap = ::onTileTap)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        launcher.onTimeChanged(LocalDateTime.now())
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_TIME_TICK)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }
        ContextCompat.registerReceiver(this, deviceFacts, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
    }

    override fun onStop() {
        unregisterReceiver(deviceFacts)
        super.onStop()
    }

    private fun onTileTap(position: Int) {
        vibrator.vibrate(VibrationEffect.createOneShot(TAP_VIBRATION_MILLIS, VibrationEffect.DEFAULT_AMPLITUDE))
        launcher.onTileTapped(position)?.let(::leaveFor)
    }

    private fun batteryPercent(intent: Intent): Int? {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        return if (level >= 0 && scale > 0) level * 100 / scale else null
    }

    private companion object {
        const val TAP_VIBRATION_MILLIS = 40L
    }
}
