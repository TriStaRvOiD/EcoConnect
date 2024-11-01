package com.sustainhive.ecoconnect.presentation.auth.navigation

import ContentWithMessageBar
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.sustainhive.ecoconnect.presentation.util.BackPress
import com.sustainhive.ecoconnect.presentation.root_navigation.Graph
import com.sustainhive.ecoconnect.presentation.auth.AuthViewModel
import com.sustainhive.ecoconnect.presentation.auth.password_reset.PasswordResetScreen
import com.sustainhive.ecoconnect.presentation.auth.sign_in.SignInScreen
import com.sustainhive.ecoconnect.presentation.auth.sign_up.SignUpScreen
import com.sustainhive.ecoconnect.presentation.util.sharedHiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rememberMessageBarState

@SuppressLint("RestrictedApi")
fun NavGraphBuilder.authNavigationGraph(navController: NavHostController) {

    navigation(
        route = Graph.Auth::class,
        startDestination = AuthScreen.Login
    ) {

        composable<AuthScreen.Login> { navBackStackEntry ->
            val authViewModel = navBackStackEntry.sharedHiltViewModel<AuthViewModel>(navController = navController)
            var showToast by remember { mutableStateOf(value = false) }
            var backPressState by remember { mutableStateOf<BackPress>(value = BackPress.Idle) }
            val messageBarState = rememberMessageBarState()

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
            SignInScreen(
                email = authViewModel.email,
                password = authViewModel.password,
                onEmailChange = {
                    authViewModel.modifyEmail(it)
                },
                onPasswordChange = {
                    authViewModel.modifyPassword(it)
                },
                authenticated = authViewModel.authenticated,
                isLoading = authViewModel.isLoading,
                messageBarState = messageBarState,
                onSignIn = {
                    authViewModel.signInToFirebase(
                        onSuccess = {
                            messageBarState.addSuccess(message = "Successfully signed in")
                            authViewModel.viewModelScope.launch {
                                delay(2000)
                                navigateToDashboard(navController = navController)
                            }
                        },
                        onFailure = { e ->
                            messageBarState.addError(exception = e)
                        },
                    )
                },
                navigateToForgotPassword = {
                    navController.navigate(AuthScreen.PasswordReset) {
                        launchSingleTop = true
                    }
                },
                navigateToSignUp = {
                    navController.navigate(AuthScreen.Register) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<AuthScreen.PasswordReset> { navBackStackEntry ->
            val authViewModel = navBackStackEntry.sharedHiltViewModel<AuthViewModel>(navController = navController)
            val isLoading = authViewModel.isLoading
            val messageBarState = rememberMessageBarState()
            ContentWithMessageBar(
                messageBarState = messageBarState,
                contentBackgroundColor = MaterialTheme.colorScheme.background
            ) {
                PasswordResetScreen(
                    email = authViewModel.email,
                    isLoading = isLoading,
                    onEmailChange = {
                        authViewModel.modifyEmail(it)
                    },
                    onResetPassword = {
                        authViewModel.resetPassword(
                            onSuccess = {
                                messageBarState.addSuccess("Sent email")
                            },
                            onFailure = { e ->
                                messageBarState.addError(e)
                            }
                        )
                    },
                    navigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable<AuthScreen.Register> { navBackStackEntry ->
            val authViewModel = navBackStackEntry.sharedHiltViewModel<AuthViewModel>(navController = navController)
            val authenticated = authViewModel.authenticated
            val isLoading = authViewModel.isLoading
            val messageBarState = rememberMessageBarState()

            ContentWithMessageBar(
                messageBarState = messageBarState,
                contentBackgroundColor = MaterialTheme.colorScheme.background
            ) {
                SignUpScreen(
                    name = authViewModel.name,
                    email = authViewModel.email,
                    password = authViewModel.password,
                    onNameChange = {
                        authViewModel.modifyName(it)
                    },
                    onEmailChange = {
                        authViewModel.modifyEmail(it)
                    },
                    onPasswordChange = {
                        authViewModel.modifyPassword(it)
                    },
                    authenticated = authenticated,
                    isLoading = isLoading,
                    messageBarState = messageBarState,
                    onSubmit = {
                        authViewModel.signUpToFirebase(
                            onSuccess = {
                                messageBarState.addSuccess(message = "Successfully signed up")
                                authViewModel.viewModelScope.launch {
                                    delay(2000)
                                    navigateToDashboard(navController = navController)
                                }
                            },
                            onFailure = { e ->
                                messageBarState.addError(exception = e)
                            }
                        )
                    },
                    navigateBack = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}

private fun navigateToDashboard(
    navController: NavHostController
) {
    navController.navigate(Graph.Dashboard) {
        popUpTo(Graph.Auth) { inclusive = true }
        launchSingleTop = true
    }
}