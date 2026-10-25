package com.andrescalco.simplelauncher.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Everything a Caretaker sets up for the Owner's Launcher. */
@Serializable
data class Configuration(val tiles: List<Tile>) {
    companion object {
        val Default = Configuration(
            tiles = listOf(
                Tile("Contatos", TileKind.Contacts, TileColor.Blue),
                Tile("WhatsApp", TileKind.App(packageName = "com.whatsapp"), TileColor.Green),
                Tile("Câmera", TileKind.Camera, TileColor.Purple),
                Tile("Fotos", TileKind.Photos, TileColor.Orange),
                Tile("Vídeos", TileKind.Videos, TileColor.Red),
                Tile("Casa", TileKind.Navigation(destinationName = "Casa"), TileColor.Teal),
            ),
        )
    }
}

@Serializable
data class Tile(val label: String, val kind: TileKind, val color: TileColor)

@Serializable
sealed interface TileKind {
    @Serializable
    @SerialName("app")
    data class App(val packageName: String) : TileKind

    @Serializable
    @SerialName("contacts")
    data object Contacts : TileKind

    @Serializable
    @SerialName("camera")
    data object Camera : TileKind

    @Serializable
    @SerialName("photos")
    data object Photos : TileKind

    @Serializable
    @SerialName("videos")
    data object Videos : TileKind

    @Serializable
    @SerialName("navigation")
    data class Navigation(val destinationName: String) : TileKind
}

/** The fixed set of colours a Caretaker can pick for a Tile. */
@Serializable
enum class TileColor { Blue, Green, Purple, Orange, Red, Teal }
