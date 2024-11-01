package com.sustainhive.ecoconnect.domain.event

import com.google.firebase.auth.FirebaseAuth
import com.sustainhive.ecoconnect.data.event.model.Event
import com.sustainhive.ecoconnect.data.event.repository.EventRepository

class GetEventsCreatedByUserUseCase(private val eventRepository: EventRepository) {
    suspend operator fun invoke(onSuccess: () -> Unit, onFailure: (Exception) -> Unit): List<Event> {
        try {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            return eventRepository.getEventsCreatedByUser(
                ownerId = userId,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
        catch (e: NullPointerException) {
            onFailure(e)
            // log-out and ask user to login again
            return emptyList()
        }
    }
}