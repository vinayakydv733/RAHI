package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AarohanPrimary,
    onPrimary = AarohanOnPrimary,
    primaryContainer = AarohanPrimaryContainer,
    onPrimaryContainer = AarohanOnPrimaryContainer,
    inversePrimary = AarohanInversePrimary,
    secondary = AarohanSecondary,
    onSecondary = AarohanOnSecondary,
    secondaryContainer = AarohanSecondaryContainer,
    onSecondaryContainer = AarohanOnSecondaryContainer,
    tertiary = AarohanTertiary,
    onTertiary = AarohanOnTertiary,
    tertiaryContainer = AarohanTertiaryContainer,
    onTertiaryContainer = AarohanOnTertiaryContainer,
    background = AarohanSurface,
    onBackground = AarohanOnSurface,
    surface = AarohanSurface,
    onSurface = AarohanOnSurface,
    surfaceVariant = AarohanSurfaceVariant,
    onSurfaceVariant = AarohanOnSurfaceVariant,
    surfaceTint = AarohanSurfaceTint,
    inverseSurface = AarohanInverseSurface,
    inverseOnSurface = AarohanInverseOnSurface,
    error = AarohanError,
    onError = AarohanOnError,
    errorContainer = AarohanErrorContainer,
    onErrorContainer = AarohanOnErrorContainer,
    outline = AarohanOutline,
    outlineVariant = AarohanOutlineVariant,
)

private val DarkColorScheme = darkColorScheme(
    primary = AarohanPrimaryFixed,
    onPrimary = AarohanOnPrimaryFixed,
    primaryContainer = AarohanPrimaryContainer,
    onPrimaryContainer = AarohanPrimaryFixedDim,
    secondary = AarohanSecondaryFixedDim,
    onSecondary = AarohanOnSecondaryFixed,
    secondaryContainer = AarohanOnSecondaryFixedVariant,
    onSecondaryContainer = AarohanSecondaryFixed,
    background = FocusDarkBg,
    onBackground = AarohanInverseOnSurface,
    surface = FocusDarkSurface,
    onSurface = AarohanInverseOnSurface,
    surfaceVariant = AarohanInverseSurface,
    onSurfaceVariant = AarohanOutlineVariant,
    outline = AarohanOutline,
    outlineVariant = AarohanOnSurfaceVariant,
)

@Composable
fun AarohanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Keep backward compatibility
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AarohanTheme(darkTheme = darkTheme, content = content)
}
