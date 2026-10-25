package com.andrescalco.simplelauncher

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.andrescalco.simplelauncher.core.BatteryIndicator
import com.andrescalco.simplelauncher.core.HomeScreenState
import com.andrescalco.simplelauncher.core.Tile
import com.andrescalco.simplelauncher.core.TileColor
import com.andrescalco.simplelauncher.core.TileKind

@Composable
fun HomeScreen(state: HomeScreenState, onTileTap: (position: Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(state.time, fontSize = 96.sp, fontWeight = FontWeight.Bold, lineHeight = 100.sp)
        Text(state.date, fontSize = 30.sp, textAlign = TextAlign.Center)
        state.battery?.let { Battery(it) }
        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            itemsIndexed(state.tiles) { position, tile ->
                TileButton(tile, onTap = { onTileTap(position) })
            }
        }
    }
}

@Composable
private fun Battery(battery: BatteryIndicator) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (battery.isLow) colors.error else colors.background)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val content = if (battery.isLow) colors.onError else colors.onBackground
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (battery.isLow) Icons.Filled.BatteryAlert else Icons.Filled.BatteryFull,
                contentDescription = null,
                tint = content,
                modifier = Modifier.size(40.dp),
            )
            Text(stringResource(R.string.battery_level, battery.percent), fontSize = 30.sp, color = content)
        }
        if (battery.isLow) {
            Text(
                stringResource(R.string.charge_phone),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = content,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TileButton(tile: Tile, onTap: () -> Unit) {
    Column(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(tile.color.toColor())
            // A long press is handled, and does nothing, so resting a finger never opens a Tile.
            .combinedClickable(
                role = Role.Button,
                onLongClick = {},
                hapticFeedbackEnabled = false,
                onClick = onTap,
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        TileIcon(tile.kind)
        Spacer(Modifier.height(8.dp))
        Text(
            tile.label,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun TileIcon(kind: TileKind) {
    val modifier = Modifier.size(72.dp)
    val appIcon = (kind as? TileKind.App)?.let { appIcon(it.packageName) }
    if (appIcon != null) {
        Image(appIcon, contentDescription = null, modifier = modifier)
    } else {
        Icon(kind.icon(), contentDescription = null, tint = Color.White, modifier = modifier)
    }
}

/** The installed app's own icon, such as the green WhatsApp logo, or null if it isn't installed. */
@Composable
private fun appIcon(packageName: String): ImageBitmap? {
    val packageManager = LocalContext.current.packageManager
    return remember(packageName) {
        runCatching { packageManager.getApplicationIcon(packageName).toBitmap().asImageBitmap() }.getOrNull()
    }
}

private fun TileKind.icon(): ImageVector = when (this) {
    is TileKind.App -> Icons.AutoMirrored.Filled.Chat
    TileKind.Contacts -> Icons.Filled.Contacts
    TileKind.Camera -> Icons.Filled.PhotoCamera
    TileKind.Photos -> Icons.Filled.Photo
    TileKind.Videos -> Icons.Filled.VideoLibrary
    is TileKind.Navigation -> Icons.Filled.Home
}

// Dark enough for white labels to stay readable.
private fun TileColor.toColor(): Color = when (this) {
    TileColor.Blue -> Color(0xFF0D47A1)
    TileColor.Green -> Color(0xFF1B5E20)
    TileColor.Purple -> Color(0xFF4A148C)
    TileColor.Orange -> Color(0xFFBF360C)
    TileColor.Red -> Color(0xFFB71C1C)
    TileColor.Teal -> Color(0xFF004D40)
}
