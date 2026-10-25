package com.andrescalco.simplelauncher

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** One fixed, light, high-contrast look, the same day and night. */
@Composable
fun SimpleTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF0D47A1),
            onPrimary = Color.White,
            background = Color.White,
            onBackground = Color.Black,
            surface = Color.White,
            onSurface = Color.Black,
            error = Color(0xFFB71C1C),
            onError = Color.White,
        ),
        content = content,
    )
}
