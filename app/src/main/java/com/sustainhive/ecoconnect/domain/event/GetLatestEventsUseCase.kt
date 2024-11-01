package com.sustainhive.ecoconnect.domain.event

import com.sustainhive.ecoconnect.data.event.model.Event
import com.sustainhive.ecoconnect.data.event.repository.EventRepository

class GetLatestEventsUseCase(private val eventRepository: EventRepository) {
    suspend operator fun invoke(count: Int): List<Event> {
        return eventRepository.getLatestEvents(count)
    }
}