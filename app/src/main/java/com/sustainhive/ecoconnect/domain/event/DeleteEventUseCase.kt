package com.sustainhive.ecoconnect.domain.event

import com.sustainhive.ecoconnect.data.event.repository.EventRepository

class DeleteEventUseCase(private val eventRepository: EventRepository) {
    suspend operator fun invoke(eventId: String) {
        eventRepository.deleteEvent(eventId)
    }
}