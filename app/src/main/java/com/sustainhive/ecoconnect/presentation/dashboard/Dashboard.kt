package com.sustainhive.ecoconnect.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sustainhive.ecoconnect.R
import com.sustainhive.ecoconnect.presentation.dashboard.navigation.DashboardNavigation
import com.sustainhive.ecoconnect.presentation.dashboard.navigation.DashboardScreen

@Composable
fun Dashboard(
    rootNavController: NavHostController,
    navigateToAuth: () -> Unit,
) {
    val navController = rememberNavController()

    val dashboardViewModel: DashboardViewModel = viewModel()
    val shouldShowBottomBar by dashboardViewModel.shouldShowBottomBar.collectAsStateWithLifecycle()

    val hapticFeedback = LocalHapticFeedback.current

    val tabs = listOf(
        BottomBarTab.Home,
        BottomBarTab.Nearby,
        BottomBarTab.Manage,
        BottomBarTab.Settings
    )

    val selectedTabIndex by dashboardViewModel.selectedItemIndex.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(navBackStackEntry) {
        if (navBackStackEntry != null && navBackStackEntry!!.destination.route != null) {
            when (navBackStackEntry!!.destination.route!!) {

                DashboardScreen.Home::class.qualifiedName -> {
                    dashboardViewModel.setSelectedItemIndex(0)
                }

                DashboardScreen.Nearby::class.qualifiedName -> {
                    dashboardViewModel.setSelectedItemIndex(1)
                }

                DashboardScreen.Manage::class.qualifiedName -> {
                    dashboardViewModel.setSelectedItemIndex(2)
                }

                DashboardScreen.Settings::class.qualifiedName -> {
                    dashboardViewModel.setSelectedItemIndex(3)
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = shouldShowBottomBar,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 500)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 500)
                )
            ) {
                BottomNavigationBar(
                    selectedTabIndex = selectedTabIndex,
                    tabs = tabs,
                    onTabSelected = { index, tab ->
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (index != selectedTabIndex)
                            navController.navigate(tab.screenRef) {
                                popUpTo(DashboardScreen.Home) { inclusive = false }
                                launchSingleTop = true
                            }
                    }
                )
            }
        }
    ) { paddingValues ->

        DashboardNavigation(
            navController = navController,
            rootNavController = rootNavController,
            paddingValues = paddingValues,
            modifyBottomBarVisibility = { value ->
                dashboardViewModel.updateBottomBarVisibleStatus(value)
            },
            logOut = {
                dashboardViewModel.signOutOfFirebase()
                navigateToAuth()
            },
        )
    }
}

sealed class BottomBarTab(
    val title: String,
    val screenRef: DashboardScreen,
    val selectedIconRef: Int,
    val unselectedIconRef: Int,
    val hasNew: Boolean,
    val badgeCount: Int? = null
) {
    data object Home : BottomBarTab(
        title = "Home",
        screenRef = DashboardScreen.Home,
        selectedIconRef = R.drawable.home_filled,
        unselectedIconRef = R.drawable.home_outlined,
        hasNew = false
    )

    data object Nearby : BottomBarTab(
        title = "Nearby",
        screenRef = DashboardScreen.Nearby,
        selectedIconRef = R.drawable.nearby_filled,
        unselectedIconRef = R.drawable.nearby_outlined,
        hasNew = false
    )

    data object Manage : BottomBarTab(
        title = "Manage",
        screenRef = DashboardScreen.Manage,
        selectedIconRef = R.drawable.manage_filled,
        unselectedIconRef = R.drawable.manage_outlined,
        hasNew = false
    )

    data object Settings : BottomBarTab(
        title = "Settings",
        screenRef = DashboardScreen.Settings,
        selectedIconRef = R.drawable.settings_filled,
        unselectedIconRef = R.drawable.settings_outlined,
        hasNew = false
    )
}

@Composable
fun BottomNavigationBar(
    selectedTabIndex: Int,
    tabs: List<BottomBarTab>,
    onTabSelected: (Int, BottomBarTab) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        tabs.forEachIndexed { index, tab ->
            val alpha by animateFloatAsState(
                targetValue = if (!isSystemInDarkTheme() || selectedTabIndex == tabs.indexOf(tab)) 1f else .65f,
                label = "alpha"
            )
            val scale by animateFloatAsState(
                targetValue = if (selectedTabIndex == tabs.indexOf(tab)) 1f else .95f,
                visibilityThreshold = .000001f,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                ),
                label = "scale"
            )
            NavigationBarItem(
                modifier = Modifier
                    .scale(scale)
                    .alpha(alpha),
                selected = selectedTabIndex == index,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = Color.Transparent
                ),
                icon = {
                    Icon(
                        imageVector =
                        if (index == selectedTabIndex)
                            ImageVector.vectorResource(
                                tab.selectedIconRef
                            )
                        else ImageVector.vectorResource(
                            tab.unselectedIconRef
                        ),
                        contentDescription = "tab ${tab.title}"
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                    )
                },
                onClick = {
                    onTabSelected(index, tab)
                }
            )
        }
    }
}