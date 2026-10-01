package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TroVietPrimary,
    onPrimary = TroVietOnPrimary,
    primaryContainer = TroVietPrimaryContainer,
    onPrimaryContainer = Color.White,
    secondary = TroVietSecondary,
    onSecondary = TroVietOnSecondary,
    secondaryContainer = TroVietSecondaryContainer,
    tertiary = TroVietTertiaryFixed,
    onTertiary = Color.Black,
    tertiaryContainer = TroVietTertiaryContainer,
    background = Color(0xFF1C1B1B),
    surface = Color(0xFF242222),
    surfaceVariant = Color(0xFF332F2E),
    onBackground = Color(0xFFF3F0EF),
    onSurface = Color(0xFFF3F0EF),
    onSurfaceVariant = Color(0xFFDCD9D9),
    outline = TroVietOutline
)

private val LightColorScheme = lightColorScheme(
    primary = TroVietPrimary,
    onPrimary = TroVietOnPrimary,
    primaryContainer = TroVietPrimaryContainer,
    onPrimaryContainer = Color.White,
    secondary = TroVietSecondary,
    onSecondary = TroVietOnSecondary,
    secondaryContainer = TroVietSecondaryContainer,
    tertiary = TroVietTertiary,
    onTertiary = TroVietOnTertiary,
    tertiaryContainer = TroVietTertiaryContainer,
    background = TroVietBackground,
    surface = TroVietSurface,
    surfaceVariant = TroVietSurfaceContainer,
    onBackground = TroVietOnSurface,
    onSurface = TroVietOnSurface,
    onSurfaceVariant = TroVietOnSurfaceVariant,
    outline = TroVietOutline,
    outlineVariant = TroVietOutlineVariant
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

