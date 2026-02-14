package com.skye.wallpapers.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skye.wallpapers.presentation.components.WallpaperCard
import com.skye.wallpapers.wallpapers.AnalogClockWallpaperService
import com.skye.wallpapers.wallpapers.GravityBallWallpaperService

@Composable
fun WallpaperSelectionScreen(
    onSelect: (Class<*>) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {

            Text(
                text = "Wallpapers",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                WallpaperCard(
                    title = "Analog Clock",
                    description = "Minimal smooth sweeping clock",
                    onClick = { onSelect(AnalogClockWallpaperService::class.java) }
                )

                WallpaperCard(
                    title = "Gravity Ball",
                    description = "Physics-based tilt interaction",
                    onClick = { onSelect(GravityBallWallpaperService::class.java) }
                )
            }
        }
    }
}

