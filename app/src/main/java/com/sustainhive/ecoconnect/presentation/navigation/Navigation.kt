package com.sustainhive.ecoconnect.presentation.navigation

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sustainhive.ecoconnect.presentation.auth.SignInScreen
import com.sustainhive.ecoconnect.presentation.auth.AuthViewModel
import com.sustainhive.ecoconnect.presentation.auth.PasswordResetScreen
import com.sustainhive.ecoconnect.presentation.auth.SignUpScreen
import com.sustainhive.ecoconnect.presentation.home.HomeScreen
import com.sustainhive.ecoconnect.presentation.util.Screen

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController
) {
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authRoute(
            onSignIn = { email, password ->
                authViewModel.signInToFirebase(
                    email = email,
                    password = password,
                    onSuccess = {
                        Log.d("Hello sign in success", "Great")
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Authentication.route) {
                                inclusive = true
                            }
                        }
                    },
                    onFailure = { e ->
                        Log.d("Hello sign in failure", e.message.toString())
                    }
                )
            },
            navigateToForgotPassword = {
                navController.navigate(Screen.PasswordReset.route)
            },
            navigateToSignUp = {
                navController.navigate(Screen.Register.route)
            }
        )

        passwordReset(
            navigateBack = {
                navController.popBackStack()
            }
        )

        register(
            onSubmit = { email, password ->
                authViewModel.signUpToFirebase(
                    email = email,
                    password = password,
                    onSuccess = {
                        Log.d("Hello sign up success", "Great")
                        navController.navigate(Screen.Home.route)
                    },
                    onFailure = { e ->
                        Log.d("Hello sign up failure", e.message.toString())
                    }
                )
            },
            navigateBack = {
                navController.popBackStack()
            },
            navigateToHome = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Authentication.route) {
                        inclusive = true
                    }
                }
            }
        )

        homeRoute(
            logOut = {
                authViewModel.logOut()
                navController.navigate(Screen.Authentication.route) {
                    popUpTo(Screen.Home.route) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

fun NavGraphBuilder.authRoute(
    onSignIn: (String, String) -> Unit,
    navigateToForgotPassword: () -> Unit,
    navigateToSignUp: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        SignInScreen(
            onSignIn = onSignIn,
            navigateToForgotPassword = navigateToForgotPassword,
            navigateToSignUp = navigateToSignUp
        )
    }
}

fun NavGraphBuilder.passwordReset(
    navigateBack: () -> Unit
) {
    composable(route = Screen.PasswordReset.route) {
        PasswordResetScreen(
            navigateBack = navigateBack
        )
    }
}

fun NavGraphBuilder.register(
    onSubmit: (String, String) -> Unit,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit
) {
    composable(
        route = Screen.Register.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        SignUpScreen(
            onSubmit = onSubmit,
            navigateBack = navigateBack,
            navigateToHome = navigateToHome
        )
    }
}

fun NavGraphBuilder.homeRoute(
    logOut: () -> Unit
) {
    composable(route = Screen.Home.route) {
        HomeScreen(logOut = logOut)
    }
}