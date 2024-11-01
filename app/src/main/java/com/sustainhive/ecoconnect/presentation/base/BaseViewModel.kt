package com.sustainhive.ecoconnect.presentation.base

import androidx.lifecycle.ViewModel
import com.sustainhive.ecoconnect.presentation.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel : ViewModel() {
    protected fun <T> MutableStateFlow<UiState<T>>.setLoading() {
        value = UiState.Loading
    }

    protected fun <T> MutableStateFlow<UiState<T>>.setSuccess(data: T) {
        value = UiState.Success(data)
    }

    protected fun <T> MutableStateFlow<UiState<T>>.setError(message: String) {
        value = UiState.Error(message)
    }
} 