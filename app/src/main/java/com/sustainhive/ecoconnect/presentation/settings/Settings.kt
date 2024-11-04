package com.sustainhive.ecoconnect.presentation.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.sustainhive.ecoconnect.R
import com.sustainhive.ecoconnect.data.user.model.User
import com.sustainhive.ecoconnect.presentation.settings.navigation.SettingsScreen
import com.sustainhive.ecoconnect.presentation.util.components.LoadingAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    internalPaddingValues: PaddingValues,
    userData: User?,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onItemClick: (SettingsScreen) -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Settings")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
        ) {
            val cardModifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(8.dp)
                .then(
                    if (!isSystemInDarkTheme()) {
                        Modifier.border(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface,
                            shape = CardDefaults.shape
                        )
                    } else Modifier
                )

            Card(
                modifier = cardModifier,
                colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                onClick = {

                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (userData == null) {
                        LoadingAnimation()
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .padding(start = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
                                model = userData.profileImage,
                                contentDescription = "Profile photo",
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(text = userData.name)
                                Text(
                                    text = userData.email,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (userData.joinDate.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Joined ${userData.joinDate}",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(
                items = listOf(
                    SettingItem(
                        ImageVector.vectorResource(R.drawable.user),
                        label = "Account",
                        screen = SettingsScreen.Account
                    ),
                    SettingItem(
                        ImageVector.vectorResource(R.drawable.devices),
                        label = "Linked Devices",
                        screen = SettingsScreen.LinkedDevices
                    ),
                    SettingItem(
                        ImageVector.vectorResource(R.drawable.permissions),
                        label = "Permissions",
                        screen = SettingsScreen.Permissions
                    )
                ),
                onItemClick = onItemClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(
                items = listOf(
                    SettingItem(
                        ImageVector.vectorResource(R.drawable.appearance),
                        label = "Appearance",
                        screen = SettingsScreen.Appearance
                    ),
                    SettingItem(
                        ImageVector.vectorResource(R.drawable.notifications),
                        label = "Notifications",
                        screen = SettingsScreen.Notifications
                    ),
                    SettingItem(
                        ImageVector.vectorResource(R.drawable.privacy),
                        label = "Privacy",
                        screen = SettingsScreen.Privacy
                    ),
                    SettingItem(
                        ImageVector.vectorResource(R.drawable.info_circle),
                        label = "About",
                        screen = SettingsScreen.About
                    )
                ),
                onItemClick = onItemClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(
                items = listOf(
                    SettingItem(
                        ImageVector.vectorResource(
                            R.drawable.feedback
                        ),
                        label = "Feedback",
                        screen = SettingsScreen.Feedback
                    ),
                ),
                onItemClick = onItemClick
            )

            Spacer(modifier = Modifier.height(internalPaddingValues.calculateBottomPadding()))
        }
    }
}

@Composable
fun SettingsSection(
    items: List<SettingItem>,
    onItemClick: (SettingsScreen) -> Unit
) {
    val cardModifier = Modifier.fillMaxWidth()
        .padding(8.dp)
        .then(
            if (!isSystemInDarkTheme()) {
                Modifier.border(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface,
                    shape = CardDefaults.shape
                )
            } else Modifier
        )
    Card(
        modifier = cardModifier,
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        items.forEachIndexed { index, item ->
            SettingRow(
                item,
                onClick = {
                    onItemClick(item.screen)
                }
            )
            if (index < items.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 55.dp),
                    thickness = 0.5.dp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun SettingRow(
    setting: SettingItem,
    onClick: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = setting.icon,
                    contentDescription = setting.label,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = setting.label,
                        fontSize = 16.sp,
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                        contentDescription = "Go to ${setting.label}",
                    )
                }
            }
        }
    }
}

data class SettingItem(
    val icon: ImageVector,
    val label: String,
    val screen: SettingsScreen
)