package com.sustainhive.ecoconnect.presentation.util

sealed class BackPress {
    data object Idle : BackPress()
    data object InitialTouch : BackPress()
}