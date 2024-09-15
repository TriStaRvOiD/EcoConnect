package com.sustainhive.ecoconnect.presentation.util

sealed class Screen(val route: String) {

    data object Authentication : Screen(route = "authentication_screen")
    data object PasswordReset: Screen(route = "password_reset_screen")
    data object Register : Screen(route = "register_screen")

    data object Home : Screen(route = "home_screen")
    data object Map : Screen(route = "map_screen")
    data object Events : Screen(route = "events_screen")
    data object Settings : Screen(route = "settings_screen")

    data object Profile: Screen(route = "profile_screen")
}