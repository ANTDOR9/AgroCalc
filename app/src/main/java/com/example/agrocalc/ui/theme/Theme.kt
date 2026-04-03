package com.example.agrocalc.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00E5FF),
    onPrimary = Color(0xFF003640),
    primaryContainer = Color(0xFF004D5E),
    onPrimaryContainer = Color(0xFF00E5FF),
    secondary = Color(0xFF00BFA5),
    onSecondary = Color(0xFF003731),
    background = Color(0xFF0A0E1A),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF121828),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF1E2538),
    onSurfaceVariant = Color(0xFF9AA0B4),
    error = Color(0xFFFF5252),
)

@Composable
fun AgroCalcTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}