package com.sustainhive.ecoconnect.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sustainhive.ecoconnect.R

@Composable
fun OnboardScreen(
    modifier: Modifier = Modifier,
    exitOnboarding: () -> Unit
) {
    Scaffold(
        modifier
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "EcoConnect",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
                Text(
                    modifier = Modifier.clickable {
                        exitOnboarding()
                    },
                    text = "Continue ->", color = Color(0xFF64B5F6)
                )
            }
            Column(
                modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 12.dp),
                ) {
                    Text(
                        "Preserve",
                        fontSize = MaterialTheme.typography.displaySmall.fontSize,

                        )
                    Text(
                        "ecosystems",
                        fontSize = MaterialTheme.typography.displaySmall.fontSize,

                        )
                    Text(
                        "through",
                        fontSize = MaterialTheme.typography.displaySmall.fontSize,
                        color = Color.Gray,

                        )
                    Text(
                        "environmental and",
                        fontSize = MaterialTheme.typography.displaySmall.fontSize,
                        color = Color.Gray,

                        )
                    Text(
                        "climate action.",
                        fontSize = MaterialTheme.typography.displaySmall.fontSize,
                        color = Color.Gray,

                        )
                }
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(R.drawable.onboarding_image),
                    contentDescription = "Nature image",
                    alpha = if (isSystemInDarkTheme()) 0.6f else 0.95f,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}