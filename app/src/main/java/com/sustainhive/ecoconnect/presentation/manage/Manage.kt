package com.sustainhive.ecoconnect.presentation.manage

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sustainhive.ecoconnect.R
import com.sustainhive.ecoconnect.data.event.model.Event
import com.sustainhive.ecoconnect.presentation.util.EventWithOrganizerName
import com.sustainhive.ecoconnect.presentation.util.components.EmptyListColumn
import com.sustainhive.ecoconnect.presentation.util.components.EventCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageScreen(
    modifier: Modifier = Modifier,
    eventsWithOrganizers: List<EventWithOrganizerName>,
    isRefreshing: Boolean,
    paddingValues: PaddingValues,
    selectedIndex: Int,
    onSegmentRefresh: (Int) -> Unit,
    navigateToEventCreation: () -> Unit,
    navigateToEventDetails: (EventWithOrganizerName) -> Unit
) {
    val options = Segment.entries.toList()
    val hapticFeedback = LocalHapticFeedback.current
    val scrollState = rememberScrollState()
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Events",
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            navigateToEventCreation()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add),
                            contentDescription = "Add event"
                        )
                    }
                }
            )
        }
    ) { values ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(values)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = "Select a category to filter your events.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Showing: ${options[selectedIndex].name.lowercase()} events",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = if (isRefreshing) "loading. . ."
                    else "${eventsWithOrganizers.size} event${if (eventsWithOrganizers.size != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SingleChoiceSegmentedButtonRow {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            label = {
                                Text(text = label.name)
                            },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = {
                                onSegmentRefresh(index)
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            selected = index == selectedIndex
                        )
                    }
                }
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = { onSegmentRefresh(selectedIndex) },
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    state = pullToRefreshState,
                ) {
                    if (!isRefreshing && eventsWithOrganizers.isEmpty())
                        EmptyListColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .padding(bottom = 50.dp)
                        )
                    else
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(eventsWithOrganizers) { eventWithOrganizer ->
                                EventCard(
                                    event = eventWithOrganizer.event,
                                    organizer = eventWithOrganizer.organizerName,
                                    onClick = {
                                        navigateToEventDetails(eventWithOrganizer)
                                    }
                                )
                            }
                        }
                }
            }
        }
    }
}