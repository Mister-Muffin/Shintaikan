package de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun getTypography(colorScheme: ColorScheme): Typography {
    val defaults = Typography()

    return Typography(
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        headlineLarge = defaults.headlineLarge.copy(
            textAlign = TextAlign.Center,
            color = colorScheme.primary,
            fontWeight = FontWeight.W600
        ),
        headlineMedium = defaults.headlineMedium.copy(
            textAlign = TextAlign.Center,
            color = colorScheme.primary,
            fontWeight = FontWeight.W500
        ),
        headlineSmall = defaults.headlineSmall.copy(
            textAlign = TextAlign.Center,
            color = colorScheme.primary
        ),
        titleLarge = defaults.titleLarge.copy(
            color = colorScheme.secondary,
            fontWeight = FontWeight(450)
        )
    )
}