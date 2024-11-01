package com.sustainhive.ecoconnect.domain.event

import com.sustainhive.ecoconnect.data.event.model.Event
import com.sustainhive.ecoconnect.data.event.repository.EventRepository

class GetSpecificEventUseCase(private val eventRepository: EventRepository) {
    suspend operator fun invoke(eventId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit): Event {
        return eventRepository.getSpecificEvent(
            eventId,
            onSuccess = onSuccess,
            onFailure = { onFailure(it) }
        )
    }
}