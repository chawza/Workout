package com.nabeelkm.workout.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nabeelkm.workout.navigation.AppNavigator
import com.nabeelkm.workout.navigation.HomeNavigator
import com.nabeelkm.workout.navigation.HomeRoute
import com.nabeelkm.workout.theme.ThemeColor
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
            IconButton(
                onClick = {
                    navigation.navigate(HomeRoute.Home)
                },
                colors = IconButtonDefaults.iconButtonColors().copy(
                    contentColor = if (currentScreen.value == HomeRoute.Home) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(Res.drawable.home),
                    contentDescription = "Home menu"
                )
            }
            IconButton(
                onClick = {
                    navigation.navigate(HomeRoute.GoalIndex)
                },
                colors = IconButtonDefaults.iconButtonColors().copy(
                    contentColor = if (currentScreen.value == HomeRoute.GoalIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(Res.drawable.goals),
                    contentDescription = "Home menu",
                )
            }
            IconButton(
                onClick = {
                    navigation.navigate(HomeRoute.Settings)
                },
                colors = IconButtonDefaults.iconButtonColors().copy(
                    contentColor = if (currentScreen.value == HomeRoute.Settings) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(Res.drawable.settings),
                    contentDescription = "Home menu"
                )
            }
        }
    }
}

@Preview
@Composable
private fun BottomNavigationPreview() {
    val navigator = HomeNavigator(SnapshotStateList(), AppNavigator(SnapshotStateList()))
    navigator.setRoot(HomeRoute.Home)
    BottomNavigation(navigator)
}
