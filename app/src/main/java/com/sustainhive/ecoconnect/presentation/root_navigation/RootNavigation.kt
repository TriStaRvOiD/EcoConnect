package com.sustainhive.ecoconnect.presentation.root_navigation

import ContentWithMessageBar
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sustainhive.ecoconnect.presentation.auth.navigation.authNavigationGraph
import com.sustainhive.ecoconnect.presentation.dashboard.Dashboard
import com.sustainhive.ecoconnect.presentation.event_edit.EventEditScreen
import com.sustainhive.ecoconnect.presentation.event_edit.EventEditViewModel
import com.sustainhive.ecoconnect.presentation.onboarding.OnboardScreen
import rememberMessageBarState

@Composable
fun RootNavigationGraph(
    startDestination: Graph,
    rootNavController: NavHostController,
    exitOnboarding: () -> Unit
) {

    NavHost(
        navController = rootNavController,
        route = Graph.Root::class,
        startDestination = startDestination
    ) {
        composable<Graph.Onboarding> {
            OnboardScreen(
                exitOnboarding = exitOnboarding
            )
        }

        authNavigationGraph(
            navController = rootNavController
        )

        composable<Graph.Dashboard> {
            Dashboard(
                rootNavController = rootNavController,
                navigateToAuth = {
                    rootNavController.navigate(Graph.Auth) {
                        popUpTo(Graph.Dashboard) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<Graph.EventEdit> {
            val eventEditViewModel: EventEditViewModel = hiltViewModel()
            val isLoading = eventEditViewModel.isLoading
            val messageBarState = rememberMessageBarState()
            ContentWithMessageBar(
                messageBarState = messageBarState,
                contentBackgroundColor = MaterialTheme.colorScheme.background
            ) {
                val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
                    if (uri != null) {
                        eventEditViewModel.addImage(uri)
                    }
                }
                EventEditScreen(
                    viewModel = eventEditViewModel,
                    isLoading = isLoading,
                    navigateBack = {
                        rootNavController.popBackStack()
                    },
                    onSaveEvent = {
                        eventEditViewModel.upsertEvent(
                            onSuccess = {
                                messageBarState.addSuccess("Successfully created event")
                                rootNavController.previousBackStackEntry?.savedStateHandle?.set(
                                    "manageRefresh",
                                    true
                                )
                                rootNavController.popBackStack()
                            },
                            onFailure = { e ->
                                messageBarState.addError(exception = e)
                            }
                        )
                    },
                    onAddImageClick = {
                        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    }
                )
            }
        }
    }
}