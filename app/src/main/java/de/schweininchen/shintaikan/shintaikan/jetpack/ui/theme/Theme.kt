package de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorPalette = darkColorScheme(
    primary = LightBlue800,
    secondary = Yellow700,

    background = Color.White,
    //surface = LightBlue800,
    onPrimary = Color.White,
    //onSecondary = Color.Black,
    //onBackground = Color.White,
    onSurface = Color.Black,
)

private val LightColorPalette = lightColorScheme(
    primary = LightBlue800,
    onPrimary = Color.White,

    secondary = Yellow700,
    background = Color.White,
    surface = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,

    )

@Composable
fun ShintaikanJetpackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }
    val typography = getTypography(colorScheme = colorScheme)
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}