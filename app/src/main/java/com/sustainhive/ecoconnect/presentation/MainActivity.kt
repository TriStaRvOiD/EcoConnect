package com.sustainhive.ecoconnect.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.sustainhive.ecoconnect.presentation.onboarding.OnboardViewModel
import com.sustainhive.ecoconnect.presentation.root_navigation.Graph
import com.sustainhive.ecoconnect.presentation.root_navigation.RootNavigationGraph
import com.sustainhive.ecoconnect.presentation.theme.EcoConnectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var keepSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { keepSplash }
        enableEdgeToEdge()
        setContent {
            val rootNavController = rememberNavController()
            val onboardViewModel: OnboardViewModel = hiltViewModel()
            val isOnboardingComplete by onboardViewModel.isOnboardingComplete.collectAsStateWithLifecycle()

            LaunchedEffect(isOnboardingComplete) {
                keepSplash = isOnboardingComplete == null
            }
            if (isOnboardingComplete != null) {
                EcoConnectTheme(
                    dynamicColor = false
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize().statusBarsPadding(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            RootNavigationGraph(
                                startDestination = if (isOnboardingComplete as Boolean) getStartDestination() else Graph.Onboarding,
                                rootNavController = rootNavController,
                                exitOnboarding = {
                                    onboardViewModel.setOnboardingStatus(isComplete = true)
                                    rootNavController.popBackStack()
                                    rootNavController.navigate(Graph.Auth) {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getStartDestination(): Graph {
    val user = FirebaseAuth.getInstance().currentUser
    return if (user != null) Graph.Dashboard
    else Graph.Auth
}