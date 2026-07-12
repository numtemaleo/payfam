package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = DeepSlateBlack,
    onPrimary = Color.White,
    secondary = VibrantLime,
    onSecondary = DeepSlateBlack,
    background = SoftCreamBackground,
    onBackground = TextPrimary,
    surface = SoftGreyCard,
    onSurface = TextPrimary,
    surfaceVariant = AccentLightGreen,
    onSurfaceVariant = DeepSlateBlack,
    error = SpendingCoral,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = VibrantLime,
    onPrimary = DeepSlateBlack,
    secondary = AccentLightGreen,
    onSecondary = DeepSlateBlack,
    background = DeepSlateBlack,
    onBackground = Color.White,
    surface = Color(0xFF1E2522),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2B3530),
    onSurfaceVariant = Color.White,
    error = SpendingCoral,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Let's force light layout styling for matching the reference, but allow toggle
    dynamicColor: Boolean = false, 
    content: @Composable () -> Unit
) {
    // We enforce an elegant premium, high-contrast theme matching the Paytin design mockup
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
