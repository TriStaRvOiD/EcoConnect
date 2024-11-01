package com.sustainhive.ecoconnect.presentation.util

import com.sustainhive.ecoconnect.data.event.model.Event

data class EventWithOrganizerName (
    val event: Event,
    val organizerName: String
)