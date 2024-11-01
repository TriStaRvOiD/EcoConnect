package com.sustainhive.ecoconnect.data.event.model

data class Event(
    val id: String = "",
    val ownerId: String = "",
    val title: String = "",
    val description: String = "",
    val images: List<ImageData> = emptyList(),
    val imageUrl: String = "",
    val location: Region = Region(),
    val creationDateTime: String = "",
    val date: String = "",
    val time: String = "",
    val category: String = "",
    val userIds: List<String> = emptyList(),
    val maxParticipants: Int = 0
)