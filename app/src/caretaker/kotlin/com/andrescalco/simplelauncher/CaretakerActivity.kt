package com.andrescalco.simplelauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/** Placeholder until the Caretaker App's screens exist. */
class CaretakerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleTheme {
                Column(
                    modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineLarge)
                    Text(stringResource(R.string.coming_soon), style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
