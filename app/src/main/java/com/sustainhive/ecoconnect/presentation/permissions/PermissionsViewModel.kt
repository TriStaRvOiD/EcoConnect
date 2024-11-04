package com.sustainhive.ecoconnect.presentation.permissions

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sustainhive.ecoconnect.presentation.permissions.util.Permission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PermissionsViewModel : ViewModel() {

    private val _permissionsList = MutableStateFlow(
        listOf(
            Permission(
                name = "Location", description = "Access your location", isGranted = false,
                permission = Manifest.permission.ACCESS_FINE_LOCATION
            ),
            Permission(
                name = "Notifications", description = "Send notifications", isGranted = false,
                permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.POST_NOTIFICATIONS else ""
            )
        )
    )
    val permissionsList = _permissionsList.asStateFlow()

    fun updateEntryInList(isGranted: Boolean, manifestPermission: String) {
        val updatedList = _permissionsList.value.map { permission ->
            if (permission.permission == manifestPermission) {
                permission.copy(isGranted = isGranted)
            } else {
                permission
            }
        }
        viewModelScope.launch {
            _permissionsList.emit(updatedList)
        }
    }
}