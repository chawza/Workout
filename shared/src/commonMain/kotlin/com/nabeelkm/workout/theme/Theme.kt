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
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import workout.shared.generated.resources.Res
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

object ThemeColor {
    val textBlack = Color(0xFF111827)
    val textGrey = Color(0xFF6b7280)
    val foreGround2 = Color(0xF374151)
    val background = Color(0xFFf8f9fb)
    val primary = Color(0xFFFF6B4A)
    val white  = Color(0xFFffffff)
    val border = Color(0xFFE5E7EB)
    val borderSoft = Color(0xFFF3F4F6)
    val onBackground = Color(0xFFFFFFFF)

    val success = Color(0xFF10B981)
    val muted = Color(0xFF6B7280)
    val danger = Color(0xFFEF4444)
}

@Composable
fun Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            background = ThemeColor.background,
            surface = ThemeColor.onBackground,
            surfaceContainer = ThemeColor.onBackground,
            surfaceContainerHigh = ThemeColor.onBackground,
            surfaceContainerHighest = ThemeColor.onBackground,
        ),
        shapes = MaterialTheme.shapes.copy(
            medium = ShapeDefaults.Medium.copy(CornerSize(10.dp))
        ),
        content = content,
        typography = Typography(
            fontFamily = spaceGrotesk(),
            titleMedium = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            bodyMedium = TextStyle(
                fontSize = 16.sp,
            ),
            bodySmall = TextStyle(
                fontSize = 14.sp,
            ),
        )
    )
}