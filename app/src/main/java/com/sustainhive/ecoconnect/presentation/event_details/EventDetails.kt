package com.sustainhive.ecoconnect.presentation.event_details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.sustainhive.ecoconnect.R
import com.sustainhive.ecoconnect.data.event.model.ImageData
import com.sustainhive.ecoconnect.presentation.util.components.LoadingAnimation

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.EventDetailsScreen(
    textModifier: Modifier,
    organizerTextModifier: Modifier,
    title: String,
    organizer: String,
    images: List<ImageData>,
    imageUrl: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isRefreshing: Boolean,
    isLoading: Boolean,
    isOwner: Boolean?,
    isMember: Boolean?,
    isError: Boolean,
    onRefresh: () -> Unit,
    joinEvent: () -> Unit,
    leaveEvent: () -> Unit,
    navigateBack: () -> Unit,
    navigateToEditScreen: () -> Unit
) {
    val organizerText = "organized by " + organizer.ifEmpty { "<>" }
    val pullToRefreshState = rememberPullToRefreshState()
    val hapticFeedback = LocalHapticFeedback.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        navigateBack()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    if (isOwner != null && isOwner)
                        IconButton(onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            navigateToEditScreen()
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.edit),
                                contentDescription = "Edit event"
                            )
                        }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                if (!isLoading)
                    onRefresh()
            },
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            state = pullToRefreshState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState()),
            ) {
                if (isError) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = "Error")
                    Text(text = "Something went wrong")
                } else if (isOwner != null && isMember != null) {
                    if (images.isNotEmpty()) {
                        val carouselState = rememberCarouselState {
                            images.size
                        }
                        HorizontalUncontainedCarousel (
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(vertical = 16.dp),
                            state = carouselState,
                            itemWidth = 300.dp,
                            itemSpacing = 12.dp,
                        ) { index ->
                            val modifier = if (index == 0) Modifier.padding(start = 8.dp) else Modifier
                            SubcomposeAsyncImage(
                                modifier = modifier
                                    .height(200.dp)
                                    .fillMaxWidth()
                                    .sharedElement(
                                        state = rememberSharedContentState(
                                            key = images[index].url
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        boundsTransform = { _, _ ->
                                            tween(durationMillis = 500)
                                        }
                                    )
                                    .maskClip(RoundedCornerShape(8.dp)),
                                model = images[index].url,
                                contentScale = ContentScale.Crop,
                                loading = {
                                    Card {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            LoadingAnimation()
                                        }
                                    }
                                },
                                contentDescription = images[index].caption
                            )
                        }
                    }
                    else if (imageUrl.isNotEmpty()){
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .padding(16.dp)
                                .sharedElement(
                                    state = rememberSharedContentState(
                                        key = imageUrl
                                    ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = { _, _ ->
                                        tween(durationMillis = 500)
                                    }
                                )
                                .clip(RoundedCornerShape(8.dp)),
                            model = imageUrl,
                            contentScale = ContentScale.Crop,
                            loading = {
                                Card {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        LoadingAnimation()
                                    }
                                }
                            },
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                modifier = textModifier,
                                text = title,
                                style = MaterialTheme.typography.headlineLarge,
                            )
                        }
                        Text(
                            modifier = organizerTextModifier.padding(top = 4.dp),
                            text = organizerText,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(modifier = Modifier.height(26.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "General event details")
                            Spacer(modifier = Modifier.height(8.dp))
                            if (!isOwner) {
                                Text(text = "General event details")
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Join status: $isMember")
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = {
                                        if (!isMember)
                                            joinEvent()
                                        else
                                            leaveEvent()
                                    },
                                    enabled = !isLoading && !isRefreshing
                                ) {
                                    if (!isMember)
                                        Text(text = "Join event")
                                    else
                                        Text(text = "Leave event")
                                    if (isLoading) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                        )
                                    }
                                }
                            } else {
                                Text(text = "Members joined list, etc.")
                            }
                        }
                    }
                }
            }
        }
    }
}