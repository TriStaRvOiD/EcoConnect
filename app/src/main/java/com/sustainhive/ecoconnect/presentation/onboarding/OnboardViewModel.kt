package com.sustainhive.ecoconnect.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sustainhive.ecoconnect.data.onboarding.OnboardingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(
    private val onboardingDataStore: OnboardingDataStore
): ViewModel() {

    private val _isOnboardingComplete = MutableStateFlow<Boolean?>(null)
    val isOnboardingComplete = _isOnboardingComplete.asStateFlow()

    init {
        observeOnboardingStatus()
    }

    private fun observeOnboardingStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            onboardingDataStore.isOnboardingComplete.collect { isComplete ->
                _isOnboardingComplete.value = isComplete
            }
        }
    }

    fun setOnboardingStatus(isComplete: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            onboardingDataStore.setOnboardingComplete(isComplete = isComplete)
        }
    }
}