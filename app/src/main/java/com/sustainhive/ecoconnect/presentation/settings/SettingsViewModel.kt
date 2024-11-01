package com.sustainhive.ecoconnect.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.sustainhive.ecoconnect.data.user.model.User
import com.sustainhive.ecoconnect.domain.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _userData = MutableStateFlow<User?>(null)
    val userData = _userData.asStateFlow()

    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        getUserDetails()
    }

    fun getUserDetails(
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
    ) {
        isRefreshing = true
        viewModelScope.launch {
            _userData.value = getUserUseCase.invoke(
                userId,
                onSuccess = {
                    isRefreshing = false
//                    onSuccess()
                },
                onFailure = {
                    isRefreshing = false
//                    onFailure(it)
                }
            )
        }
    }
}