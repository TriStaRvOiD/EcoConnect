package com.sustainhive.ecoconnect.presentation.event_edit

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mapbox.maps.extension.style.sources.generated.vectorSource
import com.sustainhive.ecoconnect.R
import com.sustainhive.ecoconnect.presentation.util.convertMillisToDate
import com.sustainhive.ecoconnect.presentation.util.formatTimeState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventEditScreen(
    viewModel: EventEditViewModel,
    isLoading: Boolean,
    navigateBack: () -> Unit,
    onSaveEvent: () -> Unit,
    onAddImageClick: () -> Unit
) {
    val imageUriList by viewModel.imageUriList.collectAsStateWithLifecycle()

    val title = viewModel.title
    val description = viewModel.description
    val location = viewModel.location
    val eventDate = viewModel.date
    val eventTime = viewModel.time

    val category = viewModel.category

    var shouldShowDatePicker by remember {
        mutableStateOf(false)
    }

    var shouldShowTimePicker by remember {
        mutableStateOf(false)
    }

    val hapticFeedback = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Create Event") },
                navigationIcon = {
                    IconButton(onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        navigateBack()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Back button"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(imageUriList) { item ->
                    AsyncImage(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        model = item.uri,
                        contentDescription = "Event image",
                        contentScale = ContentScale.Crop
                    )
                }
                item {
                    OutlinedCard(
                        modifier = Modifier
                            .size(120.dp),
                        onClick = { onAddImageClick() }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.add),
                                contentDescription = "Add Image",
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { viewModel.title = it },
                label = { Text(text = "Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { viewModel.description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = category,
                onValueChange = { viewModel.category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (shouldShowDatePicker)
                DatePickerModal(
                    onDateSelected = { date ->
                        viewModel.date = date
                    },
                    onDismiss = {
                        shouldShowDatePicker = false
                    }
                )
            OutlinedTextField(
                value = eventDate?.let { convertMillisToDate(it) } ?: "",
                onValueChange = { },
                label = { Text("Event date") },
                placeholder = { Text("DD/MM/YYYY") },
                trailingIcon = {
                    IconButton(onClick = {
                        if (!shouldShowTimePicker)
                            shouldShowDatePicker = true
                    }) {
                        Icon(
                            ImageVector.vectorResource(R.drawable.calendar),
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(eventDate) {
                        awaitEachGesture {
                            awaitFirstDown(pass = PointerEventPass.Initial)
                            val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                            if (upEvent != null) {
                                shouldShowDatePicker = true
                            }
                        }
                    },
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (shouldShowTimePicker)
                TimePicker(
                    onConfirm = { time ->
                        viewModel.time = time
                        shouldShowTimePicker = false
                    },
                    onDismiss = {
                        shouldShowTimePicker = false
                    }
                )
            OutlinedTextField(
                value = eventTime?.let { formatTimeState(it) } ?: "",
                onValueChange = { },
                label = { Text("Event time") },
                placeholder = { Text("hr/mm") },
                trailingIcon = {
                    IconButton(onClick = {
                        if (!shouldShowDatePicker)
                            shouldShowTimePicker = true
                    }) {
                        Icon(
                            ImageVector.vectorResource(R.drawable.time),
                            contentDescription = "Select time"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(eventTime) {
                        awaitEachGesture {
                            awaitFirstDown(pass = PointerEventPass.Initial)
                            val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                            if (upEvent != null) {
                                shouldShowTimePicker = true
                            }
                        }
                    },
                readOnly = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onSaveEvent,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    Text(
                        text = "Save Event"
                    )
                    if (isLoading) {
                        Spacer(modifier = Modifier.width(6.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    DatePickerDialog(
        colors = DatePickerDefaults.colors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            colors = DatePickerDefaults.colors().copy(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            state = datePickerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                TimeInput(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors().copy(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { onConfirm(timePickerState) }) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}
