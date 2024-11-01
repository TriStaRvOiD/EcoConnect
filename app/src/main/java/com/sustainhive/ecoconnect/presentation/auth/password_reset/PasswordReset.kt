package com.sustainhive.ecoconnect.presentation.auth.password_reset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sustainhive.ecoconnect.R
import com.sustainhive.ecoconnect.presentation.theme.JosefinSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetScreen(
    email: String,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onResetPassword: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
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
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Reset your password",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Enter your email address, and we will send a link to reset the password of your account.",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = Color.Gray,
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = onEmailChange,
                label = {
                    Text(text = "Email address")
                },
                textStyle = LocalTextStyle.current.copy()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onResetPassword,
                    enabled = !isLoading
                ) {
                    Text(
                        text = "Submit",
                    )
                    if (isLoading) {
                        Spacer(modifier = Modifier.width(6.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }
                }
            }
        }
    }
}