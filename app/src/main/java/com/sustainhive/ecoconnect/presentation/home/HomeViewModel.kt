package com.sustainhive.ecoconnect.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sustainhive.ecoconnect.domain.event.GetLatestEventsUseCase
import com.sustainhive.ecoconnect.domain.user.GetUserNameUseCase
import com.sustainhive.ecoconnect.presentation.util.EventWithOrganizerName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLatestEventsUseCase: GetLatestEventsUseCase,
    private val getUserNameUseCase: GetUserNameUseCase
) : ViewModel() {

    private val _eventsWithOrganizers = MutableStateFlow<List<EventWithOrganizerName>>(emptyList())
    val eventsWithOrganizers = _eventsWithOrganizers.asStateFlow()

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        loadLatestEvents()
    }

    fun loadLatestEvents(count: Int = 10) {
        viewModelScope.launch {
            isRefreshing = true
            val latestEvents = getLatestEventsUseCase(count)
            val eventsWithOrganizers = latestEvents.map { event ->
                val name = getUserNameUseCase.invoke(
                    event.ownerId,
                    onFailure = {

                    }
                )
                EventWithOrganizerName(event, name)
            }
            _eventsWithOrganizers.value = eventsWithOrganizers
            isRefreshing = false
        }
    }
}