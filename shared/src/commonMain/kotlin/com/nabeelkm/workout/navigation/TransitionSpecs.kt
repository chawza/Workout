package com.nabeelkm.workout.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay

private const val DURATION = 300
private const val DURATION_FADE = 200

fun <T : Any> slideForwardSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
    slideInHorizontally(tween(DURATION)) { it } togetherWith
        slideOutHorizontally(tween(DURATION)) { -it / 3 } + fadeOut(tween(DURATION))
}

fun <T : Any> slideBackSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
    slideInHorizontally(tween(DURATION)) { -it / 3 } + fadeIn(tween(DURATION)) togetherWith
        slideOutHorizontally(tween(DURATION)) { it }
}

fun <T : Any> crossfadeSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
    fadeIn(tween(DURATION_FADE)) togetherWith fadeOut(tween(DURATION_FADE))
}

private fun slideUpSpec(): AnimatedContentTransitionScope<Scene<*>>.() -> ContentTransform = {
    slideInVertically(tween(DURATION)) { it } togetherWith fadeOut(tween(DURATION))
}

private fun slideDownSpec(): AnimatedContentTransitionScope<Scene<*>>.() -> ContentTransform = {
    fadeIn(tween(DURATION)) togetherWith slideOutVertically(tween(DURATION)) { it }
}

fun modalTransitionMetadata(): Map<String, Any> =
    NavDisplay.transitionSpec(slideUpSpec()) + NavDisplay.popTransitionSpec(slideDownSpec())
