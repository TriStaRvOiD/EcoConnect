package com.sustainhive.ecoconnect.presentation.permissions.util

data class Permission(
    val name: String,
    val description: String,
    val permission: String,
    val isGranted: Boolean,
)