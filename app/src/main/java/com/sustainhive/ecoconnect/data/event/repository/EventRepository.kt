package com.sustainhive.ecoconnect.data.event.repository

import com.sustainhive.ecoconnect.data.event.model.Event

interface EventRepository {
    //Retrieval
    suspend fun getSpecificEvent(eventId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit): Event
    suspend fun getLatestEvents(count: Int): List<Event>
    suspend fun getTrendingEvents(): List<Event>
    suspend fun getEventsCreatedByUser(
        ownerId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ): List<Event>

    suspend fun getEventsJoinedByUser(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ): List<Event>

    //Modification
    suspend fun upsertEvent(event: Event, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
    suspend fun deleteEvent(eventId: String)
    suspend fun joinEvent(
        userId: String, eventId: String, onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    suspend fun leaveEvent(
        userId: String, eventId: String, onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )
}