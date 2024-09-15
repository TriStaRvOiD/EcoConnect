package com.sustainhive.ecoconnect.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sustainhive.ecoconnect.R
import com.sustainhive.ecoconnect.presentation.theme.JosefinSans

@Composable
fun SignInScreen(
    onSignIn: (String, String) -> Unit,
    navigateToForgotPassword: () -> Unit,
    navigateToSignUp: () -> Unit
) {
    val appLogoPainter = rememberAsyncImagePainter(R.mipmap.ic_launcher_foreground)

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(200.dp).clip(CircleShape),
                painter = appLogoPainter,
                contentDescription = "App logo"
            )
            Text(
                text = "Welcome back",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontFamily = JosefinSans
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Please sign in to continue.",
                color = Color.Gray,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontFamily = JosefinSans
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account?",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = Color.Gray,
                    fontFamily = JosefinSans
                )
                Text(text = " ", fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                Text(
                    modifier = Modifier.clickable {
                        navigateToSignUp()
                    },
                    text = "Sign up.",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = Color(0xFF64B5F6),
                    fontFamily = JosefinSans
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = {
                        Text(text = "Email address", fontFamily = JosefinSans)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Only wrap the password and the "Forgot password?" text vertically
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    label = {
                        Text(text = "Password", fontFamily = JosefinSans)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Align the "Forgot password?" text below the password field
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End) // Align the text to the right end of the width
                        .padding(top = 4.dp) // Add some space between the password field and the text
                        .clickable {
                            navigateToForgotPassword()
                        },
                    text = "Forgot password?",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = Color(0xFF64B5F6),
                    fontFamily = JosefinSans
                )
            }
            OutlinedButton(
                onClick = { onSignIn(email, password) }
            ) {
                Text("Sign in", fontFamily = JosefinSans)
            }
        }
    }
}

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onSubmit: (String, String) -> Unit,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit
) {
    val appLogoPainter = rememberAsyncImagePainter(R.mipmap.ic_launcher_foreground)

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(200.dp).clip(CircleShape),
                painter = appLogoPainter,
                contentDescription = "App logo"
            )
            Text(
                text = "Welcome",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontFamily = JosefinSans
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Create an account to continue.",
                color = Color.Gray,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontFamily = JosefinSans
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = {
                        Text(text = "Email address", fontFamily = JosefinSans)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    label = {
                        Text(text = "Password", fontFamily = JosefinSans)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            OutlinedButton(
                onClick = { onSubmit(email, password) }
            ) {
                Text("Submit", fontFamily = JosefinSans)
            }
        }
    }
}
