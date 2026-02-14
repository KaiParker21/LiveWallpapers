package com.skye.wallpapers

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.skye.wallpapers.presentation.screens.WallpaperSelectionScreen
import com.skye.wallpapers.ui.theme.WallpapersTheme

class MainActivity :
    ComponentActivity() {
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WallpapersTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPaddings ->
                    WallpaperSelectionScreen(
                        onSelect = { serviceClass ->
                            openWallpaper(serviceClass)
                        },
                        modifier = Modifier.padding(innerPaddings)
                    )
                }
            }
        }
    }
    private fun openWallpaper(serviceClass: Class<*>) {
        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
        intent.putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(this, serviceClass)
        )
        startActivity(intent)
    }
}
