package com.sustainhive.ecoconnect.presentation.dashboard.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class DashboardScreen {

    @Serializable
    data object Home : DashboardScreen()

    @Serializable
    data object Nearby : DashboardScreen()

    @Serializable
    data object Manage : DashboardScreen()

    @Serializable
    data object Settings : DashboardScreen()

    @Serializable
    data object EventDetails: DashboardScreen()
}