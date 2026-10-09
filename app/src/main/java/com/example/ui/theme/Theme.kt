package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(
        primary = GreenPrimaryDark,
        onPrimary = GreenOnPrimaryDark,
        primaryContainer = GreenPrimaryContainerDark,
        onPrimaryContainer = GreenOnPrimaryContainerDark,
        secondary = GreenSecondaryDark,
        onSecondary = GreenOnSecondaryDark,
        secondaryContainer = GreenSecondaryContainerDark,
        onSecondaryContainer = GreenOnSecondaryContainerDark,
        background = GreenBackgroundDark,
        surface = GreenSurfaceDark,
        surfaceVariant = GreenSurfaceVariantDark,
        onBackground = GreenOnSurfaceDark,
        onSurface = GreenOnSurfaceDark,
        onSurfaceVariant = GreenOnSurfaceVariantDark,
        outline = GreenOutlineDark,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = GreenPrimary,
        onPrimary = GreenOnPrimary,
        primaryContainer = GreenPrimaryContainer,
        onPrimaryContainer = GreenOnPrimaryContainer,
        secondary = GreenSecondary,
        onSecondary = GreenOnSecondary,
        secondaryContainer = GreenSecondaryContainer,
        onSecondaryContainer = GreenOnSecondaryContainer,
        background = GreenBackgroundLight,
        surface = GreenSurfaceLight,
        surfaceVariant = GreenSurfaceVariantLight,
        onBackground = GreenOnSurfaceLight,
        onSurface = GreenOnSurfaceLight,
        onSurfaceVariant = GreenOnSurfaceVariantLight,
        outline = GreenOutlineLight,
        outlineVariant = GreenOutlineVariantLight,
    )

@Composable
fun GreenCartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Default dynamicColor to false to maintain the intentional Green & White theme requested
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
  val colorScheme =
      when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
          val context = LocalContext.current
          if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
      }

  MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content,
  )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
  GreenCartTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
