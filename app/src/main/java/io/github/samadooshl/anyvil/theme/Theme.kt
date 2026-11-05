package io.github.samadooshl.anyvil.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.core.view.WindowCompat

@Composable
fun AnyvilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Use our custom dark theme colors
    val colorScheme = darkTheme
        .let { AnyvilDarkColorScheme }
        .let { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            it
        } else {
            // Fallback for older versions
            it
        } }

    val view = LocalContext.current as? Activity
    LaunchedEffect(view) {
        view?.window?.let { window ->
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AnyvilTypography,
        content = content
    )
}

// Custom Dark Color Scheme - Anyvil specific
val AnyvilDarkColorScheme = lightColorScheme(
    // Background
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceElevated,

    // Text
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,

    // Primary/Accent
    primary = Accent,
    onPrimary = Color(0xFF000000),
    primaryContainer = AccentVariant,
    onPrimaryContainer = Color(0xFF000000),

    // Secondary
    secondary = Accent,
    onSecondary = Color(0xFF000000),

    // Tertiary
    tertiary = Color(0xFF03DAC6),
    onTertiary = Color(0xFF000000),

    // Status
    error = Error,
    onError = Color(0xFF000000),

    // Surface variants
    surfaceContainer = SurfaceDark,
    surfaceContainerHigh = SurfaceElevated,
    surfaceContainerHighest = SurfaceElevated,

    // Inverse
    inverseSurface = Color(0xFFE0E0E0),
    inverseOnSurface = Color(0xFF000000),
    inversePrimary = Color(0xFFFFD54F),

    // Outline
    outline = Color(0xFF444444),
)

// Light theme (not used by default, but available)
val AnyvilLightColorScheme = lightColorScheme(
    background = Color(0xFFFAFAFA),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    primary = Accent,
    onPrimary = Color(0xFF000000),
    secondary = Accent,
    onSecondary = Color(0xFF000000),
    error = Error,
    onError = Color(0xFFFFFFFF),
    outline = Color(0xFF79747E),
)