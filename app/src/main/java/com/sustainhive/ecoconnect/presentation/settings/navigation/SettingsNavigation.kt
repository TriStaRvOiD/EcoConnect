package com.sustainhive.ecoconnect.presentation.settings.navigation

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.sustainhive.ecoconnect.R
import com.sustainhive.ecoconnect.presentation.dashboard.navigation.DashboardScreen
import com.sustainhive.ecoconnect.presentation.permissions.PermissionsScreen
import com.sustainhive.ecoconnect.presentation.permissions.PermissionsViewModel
import com.sustainhive.ecoconnect.presentation.settings.SettingsScreen
import com.sustainhive.ecoconnect.presentation.settings.SettingsViewModel
import com.sustainhive.ecoconnect.presentation.util.sharedHiltViewModel

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("RestrictedApi")
fun NavGraphBuilder.settingsNavigation(
    navController: NavController,
    internalPaddingValues: PaddingValues,
    modifyBottomBarVisibility: (Boolean) -> Unit
) {
    navigation(
        route = DashboardScreen.Settings::class,
        startDestination = SettingsScreen.Settings
    ) {
        composable<SettingsScreen.Settings>(
            exitTransition = {
                if (
                    targetState.destination.route != DashboardScreen.Manage.javaClass.canonicalName &&
                    targetState.destination.route != DashboardScreen.Nearby.javaClass.canonicalName
                )
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300)
                    )
                else
                    null
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                null
            }
        ) { navBackStackEntry ->
            LaunchedEffect(Unit) {
                modifyBottomBarVisibility(true)
            }
            val viewModel =
                navBackStackEntry.sharedHiltViewModel<SettingsViewModel>(navController = navController)
            val userData by viewModel.userData.collectAsStateWithLifecycle()
            SettingsScreen(
                userData = userData,
                internalPaddingValues = internalPaddingValues,
                isRefreshing = viewModel.isRefreshing,
                onRefresh = {
                    viewModel.getUserDetails()
                },
                onItemClick = { screen ->
                    modifyBottomBarVisibility(false)
                    navController.navigate(screen) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<SettingsScreen.Permissions>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) { navBackStackEntry ->
            BackHandler {
                navController.navigateUp()
                modifyBottomBarVisibility(true)
            }

            val viewModel =
                navBackStackEntry.sharedHiltViewModel<SettingsViewModel>(navController = navController)
            val permissionsViewModel: PermissionsViewModel = viewModel()
            val permissionList by permissionsViewModel.permissionsList.collectAsStateWithLifecycle()

            permissionList.forEach { permission ->
                val permissionState = rememberPermissionState(permission.permission)
                permissionsViewModel.updateEntryInList(
                    isGranted = permissionState.status.isGranted,
                    manifestPermission = permission.permission
                )
            }

            PermissionsScreen(
                permissions = permissionList,
                onClick = { isGranted, manifestPermission ->
                    permissionsViewModel.updateEntryInList(
                        isGranted = isGranted,
                        manifestPermission = manifestPermission
                    )
                },
                navigateBack = {
                    navController.navigateUp()
                    modifyBottomBarVisibility(true)
                },
            )
        }

        composable<SettingsScreen.Account>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            BackHandler {
                navController.navigateUp()
                modifyBottomBarVisibility(true)
            }
            PlaceholderScreen(
                title = "Account",
                navigateBack = {
                    navController.navigateUp()
                    modifyBottomBarVisibility(true)
                }
            )
        }

        composable<SettingsScreen.LinkedDevices>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            BackHandler {
                navController.navigateUp()
                modifyBottomBarVisibility(true)
            }
            PlaceholderScreen(
                title = "Linked Devices",
                navigateBack = {
                    navController.navigateUp()
                    modifyBottomBarVisibility(true)
                }
            )
        }

        composable<SettingsScreen.Appearance>(enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            )
        },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }) {
            BackHandler {
                navController.navigateUp()
                modifyBottomBarVisibility(true)
            }
            PlaceholderScreen(
                title = "Appearance",
                navigateBack = {
                    navController.navigateUp()
                    modifyBottomBarVisibility(true)
                }
            )
        }

        composable<SettingsScreen.Notifications>(enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            )
        },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }) {
            BackHandler {
                navController.navigateUp()
                modifyBottomBarVisibility(true)
            }
            PlaceholderScreen(
                title = "Notifications",
                navigateBack = {
                    navController.navigateUp()
                    modifyBottomBarVisibility(true)
                }
            )
        }

        composable<SettingsScreen.Privacy>(enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            )
        },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }) {
            BackHandler {
                navController.navigateUp()
                modifyBottomBarVisibility(true)
            }
            PlaceholderScreen(
                title = "Privacy",
                navigateBack = {
                    navController.navigateUp()
                    modifyBottomBarVisibility(true)
                }
            )
        }

        composable<SettingsScreen.About>(enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            )
        },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }) {
            BackHandler {
                navController.navigateUp()
                modifyBottomBarVisibility(true)
            }
            PlaceholderScreen(
                title = "About",
                navigateBack = {
                    navController.navigateUp()
                    modifyBottomBarVisibility(true)
                }
            )
        }

        composable<SettingsScreen.Feedback>(enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            )
        },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }) {
            BackHandler {
                navController.navigateUp()
                modifyBottomBarVisibility(true)
            }
            PlaceholderScreen(
                title = "Feedback",
                navigateBack = {
                    navController.navigateUp()
                    modifyBottomBarVisibility(true)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
private fun PlaceholderScreen(
    title: String,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.back),
                            contentDescription = "Go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Coming soon...")
        }
    }
}