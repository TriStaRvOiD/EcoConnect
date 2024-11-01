package com.sustainhive.ecoconnect.presentation.event_edit

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.sustainhive.ecoconnect.data.event.model.Region
import com.sustainhive.ecoconnect.domain.event.UpsertEventUseCase
import com.sustainhive.ecoconnect.presentation.util.convertMillisToDate
import com.sustainhive.ecoconnect.presentation.util.formatTimeState
import com.sustainhive.ecoconnect.util.UriImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventEditViewModel @Inject constructor(
    private val upsertEventUseCase: UpsertEventUseCase,
    private val contentResolver: ContentResolver
) : ViewModel() {

    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var location by mutableStateOf(Region(latitude = 0.0, longitude = 0.0))
    var date by mutableStateOf<Long?>(null)

    @OptIn(ExperimentalMaterial3Api::class)
    var time by mutableStateOf<TimePickerState?>(null)
    var category by mutableStateOf("")

    private val _imageUriList = MutableStateFlow<List<UriImage>>(emptyList())
    val imageUriList = _imageUriList.asStateFlow()

    var isLoading by mutableStateOf(false)
        private set

    fun addImage(uri: Uri, caption: String = "") {
        val imageExtension = contentResolver.getType(uri)?.split("/")?.last() ?: "jpg"
        val remoteImagePath =
            "event-images/${FirebaseAuth.getInstance().currentUser?.uid}/" +
                    "${uri.lastPathSegment}-${System.currentTimeMillis()}.$imageExtension"
        _imageUriList.value += UriImage(
            uri = uri,
            caption = caption,
            remoteImagePath = remoteImagePath
        )
    }

    fun removeImage(uriImage: UriImage) {
        _imageUriList.value = _imageUriList.value.toMutableList().apply { remove(uriImage) }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun upsertEvent(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        isLoading = true
        viewModelScope.launch {
            upsertEventUseCase.invoke(
                title = title,
                description = description,
                location = location,
                date = date?.let { convertMillisToDate(it) } ?: "",
                time = time?.let { formatTimeState(it) } ?: "",
                category = category,
                imagesUriList = _imageUriList.value,
                onSuccess = {
                    isLoading = false
                    onSuccess()
                },
                onFailure = { e ->
                    isLoading = false
                    onFailure(e)
                }
            )
        }
    }
}