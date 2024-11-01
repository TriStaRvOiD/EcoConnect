package com.sustainhive.ecoconnect.domain.event

import com.sustainhive.ecoconnect.data.event.model.Event
import com.sustainhive.ecoconnect.data.event.repository.EventRepository

class GetTrendingEventsUseCase(private val eventRepository: EventRepository) {
    suspend operator fun invoke(): List<Event> {
        return eventRepository.getTrendingEvents()
    }
}