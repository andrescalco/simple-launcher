package com.andrescalco.simplelauncher.core

/** A request to leave the Launcher, turned into an intent by the Android layer. */
sealed interface OutgoingRequest {
    data class OpenApp(val packageName: String) : OutgoingRequest
}
