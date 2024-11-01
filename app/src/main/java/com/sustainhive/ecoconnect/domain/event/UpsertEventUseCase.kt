package com.sustainhive.ecoconnect.domain.event

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sustainhive.ecoconnect.data.event.model.Event
import com.sustainhive.ecoconnect.data.event.model.ImageData
import com.sustainhive.ecoconnect.data.event.model.Region
import com.sustainhive.ecoconnect.data.event.repository.EventRepository
import com.sustainhive.ecoconnect.util.UriImage
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UpsertEventUseCase(
    private val eventRepository: EventRepository,
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        location: Region,
        date: String,
        time: String,
        category: String,
        imagesUriList: List<UriImage>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            val userId = auth.currentUser!!.uid
            val imageDataList = if (imagesUriList.isEmpty()) {
                emptyList()
            } else {
                uploadImages(imagesUriList, onFailure)
            }

            val event = Event(
                title = title,
                description = description,
                images = imageDataList,
                location = location,
                creationDateTime = Timestamp.now().toString(),
                date = date,
                time = time,
                category = category,
                ownerId = userId
            )

            eventRepository.upsertEvent(
                event,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        } catch (e: NullPointerException) {
            onFailure(Exception("Not logged in"))
            //log out
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    private suspend fun uploadImages(
        imagesUriList: List<UriImage>,
        onFailure: (Exception) -> Unit
    ): List<ImageData> = coroutineScope {
        imagesUriList.map { uriImage ->
            async {
                uploadSingleImage(uriImage, onFailure = onFailure)
            }
        }.awaitAll()
    }

    private suspend fun uploadSingleImage(
        uriImage: UriImage,
        onFailure: (Exception) -> Unit
    ): ImageData = suspendCoroutine { continuation ->
        val imagePath = storageReference.child(uriImage.remoteImagePath)
        imagePath.putFile(uriImage.uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { onFailure(it) }
                }
                imagePath.downloadUrl
            }
            .addOnSuccessListener { uri ->
                continuation.resume(ImageData(url = uri.toString(), caption = uriImage.caption))
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }
}