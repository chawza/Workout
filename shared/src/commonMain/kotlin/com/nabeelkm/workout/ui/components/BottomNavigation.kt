package com.nabeelkm.workout.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nabeelkm.workout.navigation.AppNavigator
import com.nabeelkm.workout.navigation.HomeNavigator
import com.nabeelkm.workout.navigation.HomeRoute
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import workout.shared.generated.resources.Res
import workout.shared.generated.resources.goals
import workout.shared.generated.resources.home
import workout.shared.generated.resources.settings

@Composable
fun BottomNavigation(navigation: HomeNavigator) {
    val currentScreen = navigation.currentScreenFlow.collectAsStateWithLifecycle(null)
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomNavItem(
                icon = Res.drawable.home,
                contentDescription = "Home menu",
                isSelected = currentScreen.value == HomeRoute.Home,
                onClick = { navigation.setRoot(HomeRoute.Home) }
            )
            BottomNavItem(
                icon = Res.drawable.goals,
                contentDescription = "Goals menu",
                isSelected = currentScreen.value == HomeRoute.GoalIndex,
                onClick = { navigation.setRoot(HomeRoute.GoalIndex) }
            )
            BottomNavItem(
                icon = Res.drawable.settings,
                contentDescription = "Settings menu",
                isSelected = currentScreen.value == HomeRoute.Settings,
                onClick = { navigation.setRoot(HomeRoute.Settings) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: DrawableResource,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1.0f,
        animationSpec = tween(durationMillis = 200)
    )
    val tint by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 200)
    )

    IconButton(onClick = onClick) {
        Icon(
            modifier = Modifier.size(24.dp).graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

@Preview
@Composable
private fun BottomNavigationPreview() {
    val navigator = HomeNavigator(SnapshotStateList(), AppNavigator(SnapshotStateList()))
    navigator.setRoot(HomeRoute.Home)
    BottomNavigation(navigator)
}
