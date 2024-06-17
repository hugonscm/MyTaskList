package com.example.mytasklist.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

private val LightColors = lightColorScheme(
    primary = light_primary,
    secondary = light_secondary,
    tertiary = MyPurple,
    onTertiary = MyPurple,
    onSurface = MyWhite,
    onSecondary = MyPurple,
    background = MyPurple
)


private val DarkColors = darkColorScheme(
    primary = dark_primary,
    secondary = dark_secondary,
    tertiary = light_primary,
    onTertiary = MyWhite,
    onSurface = MyBlack,
    onSecondary = MyWhite,
    background = MyBlack
)

@Composable
fun ThemeSwitcherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> {
            DarkColors
        }

        else -> {
            LightColors
        }
    }

    (LocalView.current.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
    (LocalView.current.context as Activity).window.navigationBarColor =
        colorScheme.secondary.toArgb()

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}