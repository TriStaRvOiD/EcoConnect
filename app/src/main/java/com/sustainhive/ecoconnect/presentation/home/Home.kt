package com.sustainhive.ecoconnect.presentation.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sustainhive.ecoconnect.data.event.model.Event
import com.sustainhive.ecoconnect.presentation.util.EventWithOrganizerName
import com.sustainhive.ecoconnect.presentation.util.components.EmptyListColumn
import com.sustainhive.ecoconnect.presentation.util.components.EventCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    eventsWithOrganizers: List<EventWithOrganizerName>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isRefreshing: Boolean,
    paddingValues: PaddingValues,
    onRefresh: () -> Unit,
    navigateToEventDetails: (EventWithOrganizerName) -> Unit,
    logOut: () -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .padding(start = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Eco-Connect",
                style = MaterialTheme.typography.headlineSmall,
            )
            OutlinedButton(
                onClick = logOut
            ) {
                Text(text = "Log Out")
            }
        }
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxHeight(),
            state = pullToRefreshState,
        ) {
            if (!isRefreshing) {
                if (eventsWithOrganizers.isEmpty()) {
                    EmptyListColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 60.dp)
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(
                            rememberScrollState()
                        )
                    ) {
                        LazyRow(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding())
                        ) {
                            items(eventsWithOrganizers) { eventWithOrganizer ->
                                val imageList = eventWithOrganizer.event.images
                                val imageUrl = eventWithOrganizer.event.imageUrl
                                EventCard(
                                    imageModifier = Modifier.sharedElement(
                                        state = rememberSharedContentState(
                                            key = if (imageList.isNotEmpty())
                                                imageList[0].url
                                            else if (imageUrl.isNotEmpty())
                                                imageUrl
                                            else "null"
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        boundsTransform = { _, _ ->
                                            tween(durationMillis = 500)
                                        }
                                    ),
                                    titleModifier = Modifier.sharedBounds(
                                        sharedContentState = rememberSharedContentState(
                                            key = "title/${eventWithOrganizer.event.id}/${eventWithOrganizer.event.title}"
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        boundsTransform = { _, _ ->
                                            tween(durationMillis = 500)
                                        }
                                    ),
                                    organizerTextModifier = Modifier.sharedBounds(
                                        sharedContentState = rememberSharedContentState(
                                            key = "organizerText/${eventWithOrganizer.event.id}/${eventWithOrganizer.organizerName}"
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        boundsTransform = { _, _ ->
                                            tween(durationMillis = 500)
                                        }
                                    ),
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
}