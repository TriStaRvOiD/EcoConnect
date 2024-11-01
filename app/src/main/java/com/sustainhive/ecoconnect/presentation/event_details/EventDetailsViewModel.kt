package com.sustainhive.ecoconnect.presentation.event_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.sustainhive.ecoconnect.data.event.model.Event
import com.sustainhive.ecoconnect.domain.event.GetSpecificEventUseCase
import com.sustainhive.ecoconnect.domain.event.JoinEventUseCase
import com.sustainhive.ecoconnect.domain.event.LeaveEventUseCase
import com.sustainhive.ecoconnect.presentation.util.EventWithOrganizerName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val getSpecificEventUseCase: GetSpecificEventUseCase,
    private val joinEventUseCase: JoinEventUseCase,
    private val leaveEventUseCase: LeaveEventUseCase
) : ViewModel() {

    private val _eventWithOrganizerName = MutableStateFlow(
        EventWithOrganizerName(
            event = Event(),
            organizerName = ""
        )
    )
    val eventWithOrganizerName = _eventWithOrganizerName.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isOwner = MutableStateFlow<Boolean?>(null)
    val isOwner = _isOwner.asStateFlow()

    private val _isMember = MutableStateFlow<Boolean?>(null)
    val isMember = _isMember.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    init {
        viewModelScope.launch {
            _eventWithOrganizerName.collect { withOrganizerName ->
                _isOwner.value = withOrganizerName.event.ownerId == userId
                _isMember.value = withOrganizerName.event.userIds.contains(userId)
            }
        }
    }

    fun setEvent(event: EventWithOrganizerName) {
        viewModelScope.launch {
            _eventWithOrganizerName.emit(event)
        }
    }

    fun retrieveEvent() {
        viewModelScope.launch {
            resetLoadingStates()

            val eventId = _eventWithOrganizerName.value.event.id
            if (eventId.isEmpty()) {
                setErrorState()
                return@launch
            }

            try {
                val updatedEvent = getSpecificEventUseCase.invoke(
                    eventId = eventId,
                    onSuccess = { resetLoadingStates() },
                    onFailure = {
                        setErrorState()
                    }
                )
                _eventWithOrganizerName.emit(
                    _eventWithOrganizerName.value.copy(event = updatedEvent)
                )
            } catch (e: Exception) {
                setErrorState()
            }
        }
    }

    private fun resetLoadingStates() {
        _isError.value = false
        _isRefreshing.value = false
        _isLoading.value = false
    }

    private fun setErrorState() {
        _isError.value = true
        _isRefreshing.value = false
        _isLoading.value = false
    }

    fun joinEvent(
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val eventId = _eventWithOrganizerName.value.event.id

            try {
                joinEventUseCase.invoke(
                    eventId,
                    onSuccess = {
                        retrieveEvent()
                        onSuccess()
                    },
                    onFailure = { exception ->
                        _isLoading.value = false
                        onFailure(exception)
                    }
                )
            } catch (e: Exception) {
                _isLoading.value = false
                onFailure(e)
            }
        }
    }

    fun leaveEvent(
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val eventId = _eventWithOrganizerName.value.event.id

            try {
                leaveEventUseCase.invoke(
                    eventId,
                    onSuccess = {
                        retrieveEvent()
                        onSuccess()
                    },
                    onFailure = { exception ->
                        _isLoading.value = false
                        onFailure(exception)
                    }
                )
            } catch (e: Exception) {
                _isLoading.value = false
                onFailure(e)
            }
        }
    }
}