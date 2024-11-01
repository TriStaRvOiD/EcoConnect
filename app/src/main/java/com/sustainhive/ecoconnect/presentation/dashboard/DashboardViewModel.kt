package com.sustainhive.ecoconnect.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel: ViewModel() {

    private val _selectedItemIndex = MutableStateFlow(value = 0)
    val selectedItemIndex = _selectedItemIndex.asStateFlow()

    private val _shouldShowBottomBar = MutableStateFlow(true)
    val shouldShowBottomBar = _shouldShowBottomBar.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun setSelectedItemIndex(index: Int) {
        _selectedItemIndex.value = index
    }

    fun signOutOfFirebase() {
        firebaseAuth.signOut()
    }

    fun updateBottomBarVisibleStatus(value: Boolean) {
        viewModelScope.launch {
            _shouldShowBottomBar.emit(value)
        }
    }
}