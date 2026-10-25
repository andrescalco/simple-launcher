package com.andrescalco.simplelauncher

import com.andrescalco.simplelauncher.core.Configuration
import java.io.File
import kotlinx.serialization.json.Json

/** The Configuration kept on the Owner's phone, starting from the default Tiles. */
class ConfigurationFile(directory: File) {
    private val file = File(directory, "configuration.json")
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    fun load(): Configuration {
        if (!file.exists()) save(Configuration.Default)
        return runCatching { json.decodeFromString(Configuration.serializer(), file.readText()) }
            .getOrDefault(Configuration.Default)
    }

    fun save(configuration: Configuration) {
        file.writeText(json.encodeToString(Configuration.serializer(), configuration))
    }
}
