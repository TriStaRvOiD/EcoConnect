package com.sustainhive.ecoconnect.data.event.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.sustainhive.ecoconnect.data.event.model.Event
import kotlinx.coroutines.tasks.await

class EventRepositoryImpl(firestore: FirebaseFirestore) : EventRepository {

    private val eventCollection = firestore.collection("events")

    override suspend fun getSpecificEvent(
        eventId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ): Event {
        val document = eventCollection
            .document(eventId)
            .get(Source.SERVER)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
            .await()
        var event = document.toObject(Event::class.java)
        if (event == null) {
            onFailure(Exception("Event not found"))
        } else if (event.id.isEmpty()) {
            event = event.copy(id = document.id)
        }
        return event ?: Event()
    }

    override suspend fun getLatestEvents(count: Int): List<Event> {
        return eventCollection
//            .orderBy("creationDateTime", Query.Direction.DESCENDING)
//            .limit(count.toLong())
            .get(Source.SERVER)
            .await()
            .map { document ->
                var event = document.toObject(Event::class.java)
                if (event.id.isEmpty()) {
                    event = event.copy(id = document.id)
                }
                event
            }
    }

    override suspend fun getTrendingEvents(): List<Event> {
        return eventCollection.orderBy("userIds.size", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .await()
            .map { document ->
                val event = document.toObject(Event::class.java)
                if (event.id.isEmpty()) event.copy(id = document.id) else event
            }
    }

    override suspend fun getEventsCreatedByUser(
        ownerId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ): List<Event> {
        return eventCollection.whereEqualTo("ownerId", ownerId)
            .get()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
            .await()
            .map { document ->
                val event = document.toObject(Event::class.java)
                if (event.id.isEmpty()) event.copy(id = document.id) else event
            }
    }

    override suspend fun getEventsJoinedByUser(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ): List<Event> {
        return eventCollection.whereArrayContains("userIds", userId)
            .get()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
            .await()
            .map { document ->
                val event = document.toObject(Event::class.java)
                if (event.id.isEmpty()) event.copy(id = document.id) else event
            }
    }

    override suspend fun upsertEvent(
        event: Event,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (event.id.isEmpty()) {
            // New event, add it
            val documentRef = eventCollection.document()  // Generate a new document reference
            val newEvent = event.copy(id = documentRef.id)  // Add the generated ID to the event
            documentRef.set(newEvent)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        } else {
            // Update existing event
            eventCollection.document(event.id).set(event)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }.await()
    }

    override suspend fun deleteEvent(eventId: String) {
        eventCollection.document(eventId).delete().await()
    }

    override suspend fun joinEvent(
        userId: String, eventId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        eventCollection.document(eventId).update("userIds", FieldValue.arrayUnion(userId))
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
            }
            .await()
    }

    override suspend fun leaveEvent(
        userId: String, eventId: String, onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        eventCollection.document(eventId).update("userIds", FieldValue.arrayRemove(userId))
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
            }
            .await()
    }
}