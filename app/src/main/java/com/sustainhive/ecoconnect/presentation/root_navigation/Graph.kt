package com.sustainhive.ecoconnect.presentation.root_navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Graph {

    @Serializable
    data object Root : Graph()

    @Serializable
    data object Onboarding: Graph()

    @Serializable
    data object Auth : Graph()

    @Serializable
    data object Dashboard : Graph()

    @Serializable
    data object EventEdit: Graph()
}