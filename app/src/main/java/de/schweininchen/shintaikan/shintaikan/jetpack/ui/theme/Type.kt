package de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

//Replace with your font locations
val Roboto = FontFamily.Default


@Composable
fun Typography(colorScheme: ColorScheme): Typography {
    return Typography(
        labelLarge = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.10000000149011612.sp,
            lineHeight = 20.sp,
            fontSize = 14.sp
        ),
        labelMedium = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            lineHeight = 16.sp,
            fontSize = 12.sp
        ),
        labelSmall = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            lineHeight = 16.sp,
            fontSize = 11.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W400,
            letterSpacing = 0.5.sp,
            lineHeight = 24.sp,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W400,
            letterSpacing = 0.25.sp,
            lineHeight = 20.sp,
            fontSize = 14.sp
        ),
        bodySmall = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W400,
            letterSpacing = 0.4000000059604645.sp,
            lineHeight = 16.sp,
            fontSize = 12.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W700,
            letterSpacing = 0.sp,
            lineHeight = 40.sp,
            fontSize = 42.sp,
            color = colorScheme.primary,
            textAlign = TextAlign.Center
        ),
        headlineMedium = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W600,
            letterSpacing = 0.sp,
            lineHeight = 36.sp,
            fontSize = 28.sp,
            color = colorScheme.primary,
            textAlign = TextAlign.Center
        ),
        headlineSmall = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W400,
            letterSpacing = 0.sp,
            lineHeight = 32.sp,
            fontSize = 24.sp,
            color = colorScheme.primary,
            textAlign = TextAlign.Center
        ),
        displayLarge = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W400,
            letterSpacing = -0.25.sp,
            lineHeight = 64.sp,
            fontSize = 57.sp
        ),
        displayMedium = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W400,
            letterSpacing = 0.sp,
            lineHeight = 52.sp,
            fontSize = 45.sp
        ),
        displaySmall = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W400,
            letterSpacing = 0.sp,
            lineHeight = 44.sp,
            fontSize = 36.sp
        ),
        titleLarge = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W400,
            letterSpacing = 0.sp,
            lineHeight = 28.sp,
            fontSize = 22.sp
        ),
        titleMedium = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.15000000596046448.sp,
            lineHeight = 24.sp,
            fontSize = 16.sp
        ),
        titleSmall = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.10000000149011612.sp,
            lineHeight = 20.sp,
            fontSize = 14.sp
        ),
    )
}