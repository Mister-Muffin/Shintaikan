package de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = LightBlue800,
    primaryVariant = Dark_PrimaryDark,
    secondary = Yellow700,

    background = Color.White,
    //surface = LightBlue800,
    onPrimary = Color.White,
    //onSecondary = Color.Black,
    //onBackground = Color.White,
    onSurface = Color.Black,
)

private val LightColorPalette = lightColors(
    primary = LightBlue800,
    primaryVariant = PrimaryDark,
    secondary = Yellow700,

    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,

)

@Composable
fun ShintaikanJetpackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(colors.primaryVariant)
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}