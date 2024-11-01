package com.sustainhive.ecoconnect.presentation.auth.sign_in

import ContentWithMessageBar
import MessageBarState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.sustainhive.ecoconnect.R

@Composable
fun SignInScreen(
    email: String,
    password: String,
    authenticated: Boolean,
    isLoading: Boolean,
    messageBarState: MessageBarState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    navigateToSignUp: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        ContentWithMessageBar(
            messageBarState = messageBarState,
            contentBackgroundColor = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(state = scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape),
                    painter = painterResource(R.mipmap.ic_launcher_foreground),
                    contentDescription = "App logo"
                )
                Text(
                    text = "Welcome back",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Please sign in to continue.",
                    color = Color.Gray,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
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
                    )
                    Text(text = " ", fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                    Text(
                        modifier = Modifier.clickable {
                            navigateToSignUp()
                        },
                        text = "Sign up",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = Color(0xFF64B5F6),
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
                        onValueChange = onEmailChange,
                        label = {
                            Text(text = "Email address")
                        },

                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        label = {
                            Text(text = "Password")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        )
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .padding(top = 4.dp)
                            .clickable {
                                navigateToForgotPassword()
                            },
                        text = "Forgot password?",
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = Color(0xFF64B5F6),
                        )
                }
                OutlinedButton(
                    onClick = onSignIn,
                    enabled = !isLoading && !authenticated,
                ) {
                    Text(
                        text = "Sign In",
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