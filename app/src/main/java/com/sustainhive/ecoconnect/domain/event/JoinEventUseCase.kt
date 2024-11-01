package com.sustainhive.ecoconnect.domain.event

import com.google.firebase.auth.FirebaseAuth
import com.sustainhive.ecoconnect.data.event.repository.EventRepository

class JoinEventUseCase(private val eventRepository: EventRepository) {
    suspend operator fun invoke(
        eventId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            eventRepository.joinEvent(
                userId,
                eventId,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
        catch (e: NullPointerException) {
            onFailure(e)
            // log-out and ask user to login again
        }
    }
}