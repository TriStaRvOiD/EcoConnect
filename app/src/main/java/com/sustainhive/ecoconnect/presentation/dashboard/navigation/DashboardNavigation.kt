package com.sustainhive.ecoconnect.presentation.dashboard.navigation

import ContentWithMessageBar
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sustainhive.ecoconnect.presentation.event_details.EventDetailsScreen
import com.sustainhive.ecoconnect.presentation.event_details.EventDetailsViewModel
import com.sustainhive.ecoconnect.presentation.home.HomeScreen
import com.sustainhive.ecoconnect.presentation.home.HomeViewModel
import com.sustainhive.ecoconnect.presentation.manage.ManageScreen
import com.sustainhive.ecoconnect.presentation.manage.ManageViewModel
import com.sustainhive.ecoconnect.presentation.nearby.NearbyScreen
import com.sustainhive.ecoconnect.presentation.root_navigation.Graph
import com.sustainhive.ecoconnect.presentation.settings.navigation.settingsNavigation
import com.sustainhive.ecoconnect.presentation.util.BackPress
import com.sustainhive.ecoconnect.presentation.util.sharedViewModel
import kotlinx.coroutines.delay
import rememberMessageBarState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DashboardNavigation(
    navController: NavHostController,
    rootNavController: NavHostController,
    paddingValues: PaddingValues,
    modifyBottomBarVisibility: (Boolean) -> Unit,
    logOut: () -> Unit,
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            route = Graph.Dashboard::class,
            startDestination = DashboardScreen.Home
        ) {
            composable<DashboardScreen.Home> { navBackStackEntry ->
                LaunchedEffect(Unit) {
                    modifyBottomBarVisibility(true)
                }
                val commonViewModel =
                    navBackStackEntry.sharedViewModel<DashboardCommonViewModel>(navController = navController)
                val viewModel: HomeViewModel = hiltViewModel()
                val eventsWithOrganizers by viewModel.eventsWithOrganizers.collectAsStateWithLifecycle()
                var showToast by remember { mutableStateOf(value = false) }
                var backPressState by remember { mutableStateOf<BackPress>(value = BackPress.Idle) }

                if (showToast) {
                    Toast.makeText(
                        LocalContext.current, "Go back again to exit", Toast.LENGTH_SHORT
                    ).show()
                    showToast = false
                }

                LaunchedEffect(key1 = backPressState) {
                    if (backPressState == BackPress.InitialTouch) {
                        delay(timeMillis = 3000)
                        backPressState = BackPress.Idle
                    }
                }

                BackHandler(backPressState == BackPress.Idle) {
                    backPressState = BackPress.InitialTouch
                    showToast = true
                }

                val homeRefresh =
                    navController.currentBackStackEntry?.savedStateHandle?.getStateFlow(
                        initialValue = false,
                        key = "homeRefresh"
                    )
                        ?.collectAsState()

                LaunchedEffect(homeRefresh?.value) {
                    if (homeRefresh != null && homeRefresh.value) {
                        viewModel.loadLatestEvents()
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "homeRefresh",
                            false
                        )
                    }
                }
                HomeScreen(
                    eventsWithOrganizers = eventsWithOrganizers,
                    animatedVisibilityScope = this,
                    isRefreshing = viewModel.isRefreshing,
                    paddingValues = paddingValues,
                    onRefresh = {
                        viewModel.loadLatestEvents()
                    },
                    navigateToEventDetails = { eventWithOrganizerName ->
                        commonViewModel.modifyEvent(eventWithOrganizerName)
                        navController.navigate(DashboardScreen.EventDetails)
                        modifyBottomBarVisibility(false)
                    },
                    logOut = logOut
                )
            }

            composable<DashboardScreen.Nearby> {
                LaunchedEffect(Unit) {
                    modifyBottomBarVisibility(true)
                }
                NearbyScreen(modifier = Modifier.padding(paddingValues))
            }

            composable<DashboardScreen.Manage> { navBackStackEntry ->
                LaunchedEffect(Unit) {
                    modifyBottomBarVisibility(true)
                }
                val commonViewModel =
                    navBackStackEntry.sharedViewModel<DashboardCommonViewModel>(navController = navController)
                val viewModel: ManageViewModel = hiltViewModel()
                val eventsWithOrganizers by viewModel.eventsWithOrganizers.collectAsStateWithLifecycle()
                val selectedIndex = viewModel.selectedSegment

                val manageRefresh =
                    rootNavController.currentBackStackEntry?.savedStateHandle?.getStateFlow(
                        initialValue = false,
                        key = "manageRefresh"
                    )
                        ?.collectAsState()

                LaunchedEffect(manageRefresh?.value) {
                    if (manageRefresh != null && manageRefresh.value) {
                        viewModel.onSegmentSelected(selectedIndex)
                        rootNavController.currentBackStackEntry?.savedStateHandle?.set(
                            "manageRefresh",
                            false
                        )
                    }
                }
                ManageScreen(
                    eventsWithOrganizers = eventsWithOrganizers,
                    isRefreshing = viewModel.isRefreshing,
                    paddingValues = paddingValues,
                    selectedIndex = selectedIndex,
                    onSegmentRefresh = { index ->
                        viewModel.onSegmentSelected(index)
                    },
                    navigateToEventCreation = {
                        rootNavController.navigate(Graph.EventEdit)
                    },
                    navigateToEventDetails = { eventWithOrganizerName ->
                        commonViewModel.modifyEvent(eventWithOrganizerName)
                        navController.navigate(DashboardScreen.EventDetails)
                        modifyBottomBarVisibility(false)
                    }
                )
            }

            settingsNavigation(
                navController = navController,
                internalPaddingValues = paddingValues,
                modifyBottomBarVisibility = modifyBottomBarVisibility
            )

            composable<DashboardScreen.EventDetails> { navBackStackEntry ->
                val commonViewModel =
                    navBackStackEntry.sharedViewModel<DashboardCommonViewModel>(navController = navController)
                val viewModel: EventDetailsViewModel = hiltViewModel()
                viewModel.setEvent(commonViewModel.event.value)
                val eventWithOrganizerName by viewModel.eventWithOrganizerName.collectAsStateWithLifecycle()
                val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
                val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
                val isOwner by viewModel.isOwner.collectAsStateWithLifecycle()
                val isMember by viewModel.isMember.collectAsStateWithLifecycle()
                val isError by viewModel.isError.collectAsStateWithLifecycle()
                LaunchedEffect(key1 = Unit) {
                    delay(1000)
                    viewModel.retrieveEvent()
                }
                val messageBarState = rememberMessageBarState()

                BackHandler(true) {
                    navController.navigateUp()
                    modifyBottomBarVisibility(true)
                }
                ContentWithMessageBar(
                    messageBarState = messageBarState,
                    contentBackgroundColor = MaterialTheme.colorScheme.background
                ) {
                    EventDetailsScreen(
                        textModifier = Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(
                                key = "title/${eventWithOrganizerName.event.id}/${eventWithOrganizerName.event.title}"
                            ),
                            animatedVisibilityScope = this,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 500)
                            }
                        ),
                        organizerTextModifier = Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(
                                key = "organizerText/${eventWithOrganizerName.event.id}/${eventWithOrganizerName.organizerName}"
                            ),
                            animatedVisibilityScope = this,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 500)
                            }
                        ),
                        title = eventWithOrganizerName.event.title,
                        organizer = eventWithOrganizerName.organizerName,
                        images = eventWithOrganizerName.event.images,
                        imageUrl = eventWithOrganizerName.event.imageUrl,
                        animatedVisibilityScope = this,
                        isRefreshing = isRefreshing,
                        isLoading = isLoading,
                        isOwner = isOwner,
                        isMember = isMember,
                        isError = isError,
                        onRefresh = {
                            viewModel.retrieveEvent()
                        },
                        joinEvent = {
                            viewModel.joinEvent(
                                onSuccess = {
                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                        "homeRefresh",
                                        true
                                    )
                                    messageBarState.addSuccess("Successfully joined event")
                                },
                                onFailure = { e ->
                                    messageBarState.addSuccess(e.toString())
                                }
                            )
                        },
                        leaveEvent = {
                            viewModel.leaveEvent(
                                onSuccess = {
                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                        "homeRefresh",
                                        true
                                    )
                                    messageBarState.addSuccess("Successfully left event")
                                },
                                onFailure = { e ->
                                    messageBarState.addSuccess(e.toString())
                                }
                            )
                        },
                        navigateBack = {
                            navController.navigateUp()
                            modifyBottomBarVisibility(true)
                        },
                        navigateToEditScreen = {

                        }
                    )
                }
            }
        }
    }
}