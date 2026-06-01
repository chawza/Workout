package com.nabeelkm.workout.theme

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import workout.shared.generated.resources.Res
import workout.shared.generated.resources.DM_Sans_Medium
import workout.shared.generated.resources.DM_Sans_Regular
import workout.shared.generated.resources.DM_Sans_SemiBold
import workout.shared.generated.resources.SpaceGrotesk_Bold
import workout.shared.generated.resources.SpaceGrotesk_Regular
import workout.shared.generated.resources.SpaceGrotesk_SemiBold


@Composable
fun spaceGrotesk(): FontFamily {
    return FontFamily(
        Font(Res.font.SpaceGrotesk_Regular, FontWeight.Medium),
        Font(Res.font.SpaceGrotesk_SemiBold, FontWeight.SemiBold),
        Font(Res.font.SpaceGrotesk_Bold, FontWeight.Bold),
    )
}

@Composable
fun dmSans(): FontFamily {
    return FontFamily(
        Font(Res.font.DM_Sans_Regular, FontWeight.Normal),
        Font(Res.font.DM_Sans_Medium, FontWeight.Medium),
        Font(Res.font.DM_Sans_SemiBold, FontWeight.SemiBold),
    )
}

object AppColor {
    val bg = Color(0xFFF8F9FB)
    val surface = Color(0xFFFFFFFF)
    val surfaceWarm = Color(0xFFFFF5F3)
    val fg = Color(0xFF111827)
    val fg2 = Color(0xFF374151)
    val muted = Color(0xFF6B7280)
    val meta = Color(0xFFFF6B4A)
    val border = Color(0xFFE5E7EB)
    val borderSoft = Color(0xFFF3F4F6)
    val accent = Color(0xFFFF6B4A)
    val accentOn = Color(0xFFFFFFFF)
    val accentSoft = Color(0x1AFF6B4A)
    val accentHover = Color(0xFFE85A3A)
    val accentActive = Color(0xFFD14E30)
    val success = Color(0xFF10B981)
    val warn = Color(0xFFF59E0B)
    val danger = Color(0xFFEF4444)

    // Goal-category palette (DESIGN.md). Add the rest here as goal theming is wired up.
    val blue = Color(0xFF3B82F6)
}

object AppText {
    val xs = 12.sp
    val sm = 14.sp
    val base = 16.sp
    val lg = 18.sp
    val xl = 22.sp
    val xl2 = 28.sp
}

object AppSpace {
    val s1 = 4.dp
    val s2 = 8.dp
    val s3 = 12.dp
    val s4 = 16.dp
    val s5 = 24.dp
    val s6 = 32.dp
    val s8 = 48.dp
}

object AppRadius {
    val sm = 6.dp
    val md = 10.dp
    val lg = 14.dp
    val pill = 999.dp
}

object ThemeColor {
    val textBlack = AppColor.fg
    val textGrey = AppColor.muted
    val foreGround2 = AppColor.fg2
    val background = AppColor.bg
    val primary = AppColor.accent
    val white = AppColor.surface
    val border = AppColor.border
    val borderSoft = AppColor.borderSoft
    val onBackground = AppColor.surface

    val success = AppColor.success
    val muted = AppColor.muted
    val danger = AppColor.danger
}

@Composable
fun Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = ThemeColor.primary,
            onPrimary = ThemeColor.white,
            background = ThemeColor.background,
            onBackground = ThemeColor.textBlack,
            surface = ThemeColor.onBackground,
            onSurface = ThemeColor.textBlack,
            surfaceContainer = ThemeColor.onBackground,
            surfaceContainerHigh = ThemeColor.onBackground,
            surfaceContainerHighest = ThemeColor.onBackground,
            error = ThemeColor.danger,
            onError = ThemeColor.white,
            outline = ThemeColor.border,
            outlineVariant = ThemeColor.borderSoft,
            surfaceVariant = ThemeColor.borderSoft,
            onSurfaceVariant = ThemeColor.textGrey,
        ),
        shapes = MaterialTheme.shapes.copy(
            medium = ShapeDefaults.Medium.copy(CornerSize(10.dp))
        ),
        content = content,
        typography = Typography(
            displayLarge = TextStyle(
                fontFamily = spaceGrotesk(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.018).em
            ),
            headlineLarge = TextStyle(
                fontFamily = spaceGrotesk(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.018).em
            ),
            titleLarge = TextStyle(
                fontFamily = spaceGrotesk(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.018).em
            ),
            titleMedium = TextStyle(
                fontFamily = spaceGrotesk(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.018).em
            ),
            titleSmall = TextStyle(
                fontFamily = dmSans(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),
            bodyLarge = TextStyle(
                fontFamily = dmSans(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            bodyMedium = TextStyle(
                fontFamily = dmSans(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            bodySmall = TextStyle(
                fontFamily = dmSans(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
            labelLarge = TextStyle(
                fontFamily = dmSans(),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            ),
            labelMedium = TextStyle(
                fontFamily = dmSans(),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            ),
            labelSmall = TextStyle(
                fontFamily = dmSans(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            ),
        )
    )
}