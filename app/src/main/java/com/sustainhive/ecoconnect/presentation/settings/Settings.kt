package com.sustainhive.ecoconnect.presentation.settings

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
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
import com.sustainhive.ecoconnect.presentation.util.components.LoadingAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    internalPaddingValues: PaddingValues,
    userData: User?,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(internalPaddingValues),
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
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(8.dp),
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
                                .padding(16.dp)
                                .padding(start = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(87.dp)
                                    .clip(CircleShape),
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
                    SettingItem(ImageVector.vectorResource(R.drawable.user), "Account"),
                    SettingItem(
                        ImageVector.vectorResource(R.drawable.devices),
                        "Linked Devices"
                    ),
                    SettingItem(
                        ImageVector.vectorResource(R.drawable.permissions),
                        "Permissions"
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(
                items = listOf(
                    SettingItem(
                        ImageVector.vectorResource(R.drawable.appearance),
                        "Appearance"
                    ),
                    SettingItem(
                        ImageVector.vectorResource(R.drawable.notifications),
                        "Notifications"
                    ),
                    SettingItem(ImageVector.vectorResource(R.drawable.privacy), "Privacy"),
                    SettingItem(ImageVector.vectorResource(R.drawable.info_circle), "About")
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(
                items = listOf(
                    SettingItem(ImageVector.vectorResource(R.drawable.feedback), "Feedback"),
                )
            )
        }
    }
}

@Composable
fun SettingsSection(items: List<SettingItem>) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items.forEach { item ->
            SettingRow(item)
            HorizontalDivider(
                modifier = Modifier.padding(start = 55.dp),
                thickness = 0.5.dp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun SettingRow(
    setting: SettingItem,
    onClick: () -> Unit = {}
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
                    modifier = Modifier.size(24.dp)
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
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}

data class SettingItem(val icon: ImageVector, val label: String)