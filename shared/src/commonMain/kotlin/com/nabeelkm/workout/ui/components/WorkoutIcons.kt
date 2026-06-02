package com.nabeelkm.workout.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/* ──────────────────────────────────────────────────────────────────
 * Workout / goal iconography.
 *
 * Ported from the design bundle's inline-SVG icon set (data.jsx). Every
 * icon is a stroke-only vector on a 24×24 grid with round caps/joins,
 * mirroring the prototype's `Icon` component. Stroke colour is left
 * black so Material's `Icon(tint = …)` can recolour it from the
 * container, exactly like the prototype's `currentColor` behaviour.
 * ────────────────────────────────────────────────────────────────── */

/**
 * Goal-activity icons. SVG `<circle>` elements from the prototype are
 * expressed here as two-arc sub-paths so the whole glyph fits in a
 * single path string.
 *
 * Note: [strokeVector] lives *inside* this object on purpose. If it were a
 * top-level function, building these icons would force the file-class to
 * initialize, which in turn builds [GOAL_ICONS] (which reads these very
 * properties before they're assigned) — a static-init cycle that NPEs.
 */
object WorkoutIcons {
    private fun strokeVector(name: String, pathData: String, strokeWidth: Float = 1.8f): ImageVector =
        ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            addPath(
                pathData = addPathNodes(pathData),
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            )
        }.build()

    val Run = strokeVector("run", "M13 2L3 14h9l-1 8 10-12h-9l1-8z")
    val Walk = strokeVector(
        "walk",
        "M11.4 4 a1.6 1.6 0 1 0 3.2 0 a1.6 1.6 0 1 0 -3.2 0Z " +
            "M13 7l-2 4 3 3 1 6M11 11l-3 2-2 4M14 14l4 1",
    )
    val Strength = strokeVector(
        "strength",
        "M6.5 6.5l11 11M21 21l-1-1M3 3l1 1M18 22l4-4M2 6l4-4M7 17l-5 5M22 7l-5-5",
    )
    val Bike = strokeVector(
        "bike",
        "M2 17.5 a3.5 3.5 0 1 0 7 0 a3.5 3.5 0 1 0 -7 0Z " +
            "M15 17.5 a3.5 3.5 0 1 0 7 0 a3.5 3.5 0 1 0 -7 0Z " +
            "M5.5 17.5L9 9h4.5M18.5 17.5L15 9M9 9l2-3h2",
    )
    val Swim = strokeVector(
        "swim",
        "M2 16c1.5-1.5 3.5-1.5 5 0s3.5 1.5 5 0 3.5-1.5 5 0 3.5 1.5 5 0 " +
            "M2 21c1.5-1.5 3.5-1.5 5 0s3.5 1.5 5 0 3.5-1.5 5 0 3.5 1.5 5 0 " +
            "M10 7 a2 2 0 1 0 4 0 a2 2 0 1 0 -4 0Z M8 13l4-2 4 2",
    )
    val Yoga = strokeVector(
        "yoga",
        "M10.4 4 a1.6 1.6 0 1 0 3.2 0 a1.6 1.6 0 1 0 -3.2 0Z " +
            "M12 7v5M7 12l5 3 5-3M8 22l4-8 4 8",
    )
    val Flame = strokeVector(
        "flame",
        "M12 2c1 4 4 5 4 9a4 4 0 01-8 0c0-1.5.5-2.5 1-3 0 1.5 1 2 1.5 2 0-2 1-4 1.5-8z",
    )
    val Target = strokeVector(
        "target",
        "M3 12 a9 9 0 1 0 18 0 a9 9 0 1 0 -18 0Z M8 12 a4 4 0 1 0 8 0 a4 4 0 1 0 -8 0Z",
    )

    // UI glyphs used by the picker / management screen.
    val Close = strokeVector("x", "M18 6L6 18M6 6l12 12")
    val Check = strokeVector("check", "M20 6L9 17L4 12")
    val Chevron = strokeVector("chevron", "M9 18l6-6-6-6")
    val Back = strokeVector("back", "M19 12H5M12 19l-7-7 7-7")
    val Download = strokeVector("download", "M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4M7 10l5 5 5-5M12 15V3")
    val Plus = strokeVector("plus", "M12 5v14M5 12h14")
}

/** A selectable goal icon: stable id, display label, and its glyph. */
data class GoalIconDef(val id: String, val displayName: String, val image: ImageVector)

val GOAL_ICONS: List<GoalIconDef> = listOf(
    GoalIconDef("run", "Running", WorkoutIcons.Run),
    GoalIconDef("walk", "Walking", WorkoutIcons.Walk),
    GoalIconDef("strength", "Strength", WorkoutIcons.Strength),
    GoalIconDef("bike", "Cycling", WorkoutIcons.Bike),
    GoalIconDef("swim", "Swimming", WorkoutIcons.Swim),
    GoalIconDef("yoga", "Yoga", WorkoutIcons.Yoga),
    GoalIconDef("flame", "Cardio", WorkoutIcons.Flame),
    GoalIconDef("target", "General", WorkoutIcons.Target),
)

/** Resolve a goal icon id to its glyph, falling back to the general target. */
fun goalIconVector(id: String): ImageVector =
    GOAL_ICONS.firstOrNull { it.id == id }?.image ?: WorkoutIcons.Target

/** A selectable goal colour: stable id + hex string (uppercase #RRGGBB). */
data class GoalColorDef(val id: String, val hex: String)

val GOAL_COLORS: List<GoalColorDef> = listOf(
    GoalColorDef("teal", "#0D9488"),
    GoalColorDef("green", "#059669"),
    GoalColorDef("blue", "#3B82F6"),
    GoalColorDef("indigo", "#6366F1"),
    GoalColorDef("purple", "#7C3AED"),
    GoalColorDef("pink", "#EC4899"),
    GoalColorDef("red", "#EF4444"),
    GoalColorDef("orange", "#F97316"),
    GoalColorDef("amber", "#F59E0B"),
    GoalColorDef("slate", "#64748B"),
)

/** Default icon id + colour hex when nothing has been picked yet. */
const val DEFAULT_GOAL_ICON: String = "target"
const val DEFAULT_GOAL_COLOR: String = "#6366F1"

/** Parse a "#RRGGBB" hex string into a Compose [Color]. */
fun hexToColor(hex: String): Color {
    val cleaned = hex.removePrefix("#")
    val rgb = cleaned.toLong(16)
    return Color(0xFF000000 or rgb)
}

/** A picked icon + colour pair. */
data class IconColorChoice(val icon: String, val color: String)

/**
 * Smart default icon + colour guessed from a goal's name — mirrors the
 * prototype's `guessIconColor()` so freshly-typed goals get a sensible
 * glyph before the user opens the picker.
 */
fun guessIconColor(name: String): IconColorChoice {
    val n = name.lowercase()
    fun has(vararg keys: String) = keys.any { n.contains(it) }
    return when {
        has("swim", "pool", "lap") -> IconColorChoice("swim", "#3B82F6")
        has("bike", "cycl", "ride", "spin") -> IconColorChoice("bike", "#F97316")
        has("yoga", "stretch", "flex", "pilat") -> IconColorChoice("yoga", "#EC4899")
        has("walk", "hike", "step") -> IconColorChoice("walk", "#059669")
        has("strength", "arm", "lift", "gym", "weight", "push", "pull", "muscle") ->
            IconColorChoice("strength", "#7C3AED")
        has("cardio", "hiit", "burn", "circuit") -> IconColorChoice("flame", "#EF4444")
        has("run", "jog", "sprint", "marathon", "5k", "10k") -> IconColorChoice("run", "#0D9488")
        else -> IconColorChoice(DEFAULT_GOAL_ICON, DEFAULT_GOAL_COLOR)
    }
}
