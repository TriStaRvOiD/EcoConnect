package com.sustainhive.ecoconnect.presentation.util.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.sustainhive.ecoconnect.R
import com.sustainhive.ecoconnect.data.event.model.Event
import com.sustainhive.ecoconnect.data.event.model.Region

@Composable
fun EventCard(
    imageModifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
    organizerTextModifier: Modifier = Modifier,
    event: Event,
    organizer: String,
    onClick: () -> Unit = {}
) {
    val organizerText = "organized by " + organizer.ifEmpty { "<>" }

    val infiniteTransition = rememberInfiniteTransition(label = "text transition")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5000,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "progress"
    )

    val hapticFeedback = LocalHapticFeedback.current
    OutlinedCard(
        modifier = Modifier
            .width(300.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = Color.Gray
        ),
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val imageUrl = when {
                event.images.isNotEmpty() -> event.images.first().url
                event.imageUrl.isNotEmpty() -> event.imageUrl
                else -> null
            }

            if (imageUrl != null) {
                SubcomposeAsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = imageModifier
                        .height(160.dp),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingAnimation()
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = titleModifier,
                        text = event.title,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                    )
                    Text(
                        modifier = organizerTextModifier.padding(top = 1.dp),
                        text = organizerText,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (event.description.isNotEmpty()) {
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (event.location != Region(latitude = 0.0, longitude = 0.0)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = event.location.latitude.toString(),
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = event.location.longitude.toString(),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (event.date.isNotEmpty() && event.time.isNotEmpty()) {
                        AnimatedContent(
                            targetState = progress >= 0.5f,
                            transitionSpec = {
                                fadeIn(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessVeryLow  // Softer transition
                                    )
                                ) togetherWith fadeOut(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessVeryLow
                                    )
                                )
                            },
                            label = "text content"
                        ) { showSecondText ->
                            if (!showSecondText) {
                                Text(
                                    modifier = Modifier.padding(top = 4.dp),
                                    text = "At ${event.time} on ${event.date} ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                                )
                            } else {
                                Text(
                                    modifier = Modifier.padding(top = 4.dp),
                                    text = "15 km away",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                    else {
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = "15 km away",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                        )
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                        contentDescription = "Event details",
                    )
                }
            }
        }
    }
}