package com.sustainhive.ecoconnect.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sustainhive.ecoconnect.R

val JosefinSans = FontFamily(
    Font(R.font.josefinsans_regular),
    Font(R.font.josefinsans_thin, FontWeight.Thin),
    Font(R.font.josefinsans_bold, FontWeight.Bold),
    Font(R.font.josefinsans_semibold, FontWeight.SemiBold),
    Font(R.font.josefinsans_light, FontWeight.Light),
    Font(R.font.josefinsans_extralight, FontWeight.ExtraLight),
    Font(R.font.josefinsans_italic),
    Font(R.font.josefinsans_bold_italic),
    Font(R.font.josefinsans_semibold_italic),
    Font(R.font.josefinsans_light_italic),
    Font(R.font.josefinsans_extralight_italic),
    Font(R.font.josefinsans_thin_italic),
)

private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = JosefinSans),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = JosefinSans),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = JosefinSans),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = JosefinSans),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = JosefinSans),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = JosefinSans),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = JosefinSans),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = JosefinSans),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = JosefinSans),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = JosefinSans),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = JosefinSans),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = JosefinSans),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = JosefinSans),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = JosefinSans),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = JosefinSans)
)