package com.sustainhive.ecoconnect.presentation.settings.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SettingsScreen {

    @Serializable
    data object Settings: SettingsScreen()

    @Serializable
    data object Account: SettingsScreen()

    @Serializable
    data object LinkedDevices: SettingsScreen()

    @Serializable
    data object Permissions: SettingsScreen()

    @Serializable
    data object Appearance: SettingsScreen()

    @Serializable
    data object Notifications: SettingsScreen()

    @Serializable
    data object Privacy: SettingsScreen()

    @Serializable
    data object About: SettingsScreen()

    @Serializable
    data object Feedback: SettingsScreen()
}
