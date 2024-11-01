package com.sustainhive.ecoconnect.presentation.auth.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthScreen {

    @Serializable
    data object Login : AuthScreen()

    @Serializable
    data object PasswordReset : AuthScreen()

    @Serializable
    data object Register : AuthScreen()
}