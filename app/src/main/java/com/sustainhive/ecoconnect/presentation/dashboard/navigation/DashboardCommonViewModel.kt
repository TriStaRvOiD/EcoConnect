package com.sustainhive.ecoconnect.presentation.dashboard.navigation

import androidx.lifecycle.ViewModel
import com.sustainhive.ecoconnect.data.event.model.Event
import com.sustainhive.ecoconnect.presentation.util.EventWithOrganizerName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardCommonViewModel: ViewModel() {

    private val _event = MutableStateFlow(EventWithOrganizerName(
        event = Event(),
        organizerName = ""
    ))
    val event = _event.asStateFlow()

    fun modifyEvent(event: EventWithOrganizerName) {
        _event.value = event
    }
}