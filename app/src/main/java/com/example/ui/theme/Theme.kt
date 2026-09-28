package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val StudioColorScheme = darkColorScheme(
    primary = StudioNeonViolet,
    onPrimary = Color.White,
    primaryContainer = StudioDarkSurfaceElevated,
    onPrimaryContainer = StudioNeonVioletLight,

    secondary = StudioNeonCyan,
    onSecondary = Color(0xFF021720),
    secondaryContainer = Color(0xFF132A38),
    onSecondaryContainer = Color(0xFF67E8F9),

    tertiary = StudioNeonPink,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF3B1527),
    onTertiaryContainer = Color(0xFFF9A8D4),

    background = StudioDarkBg,
    onBackground = StudioTextPrimary,

    surface = StudioDarkSurface,
    onSurface = StudioTextPrimary,
    surfaceVariant = StudioDarkSurfaceVariant,
    onSurfaceVariant = StudioTextSecondary,

    outline = StudioBorder,
    outlineVariant = Color(0xFF1E283D)
)

@Composable
fun VisionAITheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = StudioColorScheme,
        typography = Typography,
        content = content
    )
}
