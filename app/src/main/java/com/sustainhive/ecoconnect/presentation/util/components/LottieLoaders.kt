package com.sustainhive.ecoconnect.presentation.util.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sustainhive.ecoconnect.R

@Composable
fun LottieLoader(
    modifier: Modifier = Modifier,
    resId: Int,
    loop: Boolean = false
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(composition = composition, reverseOnRepeat = loop)
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { progress },
    )
}

@Composable
fun EmptyListColumn(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieLoader(
            modifier = Modifier.size(200.dp),
            resId = R.raw.empty_list
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "No events found",
        )
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieLoader(
            modifier = Modifier.size(500.dp),
            resId = R.raw.loading_test,
            loop = true
        )
    }
}