package com.sustainhive.ecoconnect.presentation.permissions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sustainhive.ecoconnect.R
import com.sustainhive.ecoconnect.presentation.permissions.util.Permission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    permissions: List<Permission>,
    onClick: (Boolean, String) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Permissions") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Go back"
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
        ) {
            permissions.forEach { permission ->
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        onClick(isGranted, permission.permission)
                    }
                )
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    enabled = !permission.isGranted,
                    onClick = {
                        permissionLauncher.launch(permission.permission)
                    }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = permission.name, style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = permission.description, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (permission.isGranted) "Granted" else "Not Granted",
                            color = if (permission.isGranted) Color.Green else Color.Red
                        )
                    }
                }
            }
        }
    }
}