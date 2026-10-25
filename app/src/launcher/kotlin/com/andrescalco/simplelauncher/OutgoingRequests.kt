package com.andrescalco.simplelauncher

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.andrescalco.simplelauncher.core.OutgoingRequest

/** Turns a request to leave the Launcher into the intent that opens the other app. */
fun Context.leaveFor(request: OutgoingRequest) {
    val intent: Intent? = when (request) {
        is OutgoingRequest.OpenApp -> packageManager.getLaunchIntentForPackage(request.packageName)
    }
    try {
        if (intent != null) return startActivity(intent)
    } catch (_: ActivityNotFoundException) {
    }
    Toast.makeText(this, R.string.app_not_found, Toast.LENGTH_LONG).show()
}
