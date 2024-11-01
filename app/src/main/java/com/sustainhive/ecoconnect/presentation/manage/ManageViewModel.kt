package com.sustainhive.ecoconnect.presentation.manage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sustainhive.ecoconnect.data.event.model.Event
import com.sustainhive.ecoconnect.domain.event.GetEventsCreatedByUserUseCase
import com.sustainhive.ecoconnect.domain.event.GetEventsJoinedByUserUseCase
import com.sustainhive.ecoconnect.domain.user.GetUserNameUseCase
import com.sustainhive.ecoconnect.presentation.util.EventWithOrganizerName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageViewModel @Inject constructor(
    private val getUserNameUseCase: GetUserNameUseCase,
    private val getJoinedEventsUseCase: GetEventsJoinedByUserUseCase,
    private val getCreatedEventsUseCase: GetEventsCreatedByUserUseCase,
) : ViewModel() {

    private val _eventsWithOrganizers = MutableStateFlow<List<EventWithOrganizerName>>(emptyList())
    val eventsWithOrganizers = _eventsWithOrganizers.asStateFlow()

    var isRefreshing by mutableStateOf(false)
        private set

    var selectedSegment by mutableIntStateOf(0)
        private set

    fun onSegmentSelected(index: Int) {
        selectedSegment = index
        when (Segment.entries[index]) {
            Segment.All -> getAllEvents()
            Segment.Joined -> getJoinedEvents()
            Segment.Created -> getCreatedEvents()
            Segment.Expired -> getExpiredEvents()
        }
    }

    init {
        getAllEvents()
    }

    private fun getAllEvents() {
        viewModelScope.launch {
            isRefreshing = true
            try {
                val createdEvents = getCreatedEventsUseCase.invoke(
                    onSuccess = {

                    },
                    onFailure = {
                        isRefreshing = false
                    }
                )
                val joinedEvents = getJoinedEventsUseCase.invoke(
                    onSuccess = {

                    },
                    onFailure = {
                        isRefreshing = false
                    }
                )
                val combinedEvents = (createdEvents + joinedEvents)
                val eventsWithOrganizers = combinedEvents.map { event ->
                    val name = getUserNameUseCase.invoke(
                        event.ownerId,
                        onFailure = {

                        }
                    )
                    EventWithOrganizerName(event, name)
                }
                _eventsWithOrganizers.value = eventsWithOrganizers
                isRefreshing = false
//                _events.value = (createdEvents + joinedEvents).distinctBy { it.id } // Combine and remove duplicates
            } catch (e: Exception) {
                isRefreshing = false
                // Handle any error that occurs during fetching
            } finally {
                isRefreshing = false
            }
        }
    }

    private fun getJoinedEvents(){
        viewModelScope.launch {
            isRefreshing = true
            val joinedEvents = getJoinedEventsUseCase.invoke(
                onSuccess = {

                },
                onFailure = {
                    isRefreshing = false
                }
            )
            val eventsWithOrganizers = joinedEvents.map { event ->
                val name = getUserNameUseCase.invoke(
                    event.ownerId,
                    onFailure = {
                        isRefreshing = false
                    }
                )
                EventWithOrganizerName(event, name)
            }
            _eventsWithOrganizers.value = eventsWithOrganizers
            isRefreshing = false
        }
    }

    private fun getCreatedEvents() {
        viewModelScope.launch {
            isRefreshing = true
            val createdEvents = getCreatedEventsUseCase.invoke(
                onSuccess = {

                },
                onFailure = {
                    isRefreshing = false
                }
            )
            val eventsWithOrganizers = createdEvents.map { event ->
                val name = getUserNameUseCase.invoke(
                    event.ownerId,
                    onFailure = {
                        isRefreshing = false
                    }
                )
                EventWithOrganizerName(event, name)
            }
            _eventsWithOrganizers.value = eventsWithOrganizers
            isRefreshing = false
        }
    }

    private fun getExpiredEvents() {
        _eventsWithOrganizers.value = emptyList()
    }
}

enum class Segment {
    All, Joined, Created, Expired
}