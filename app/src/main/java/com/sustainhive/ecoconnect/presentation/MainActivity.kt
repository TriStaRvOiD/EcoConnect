package com.sustainhive.ecoconnect.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.sustainhive.ecoconnect.presentation.navigation.SetupNavGraph
import com.sustainhive.ecoconnect.presentation.theme.EcoConnectTheme
import com.sustainhive.ecoconnect.presentation.util.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        setContent {
            EcoConnectTheme(
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize(),) {
                    SetupNavGraph(
                        startDestination = getStartDestination(),
                        navController = navController
                    )
                }
            }
        }
    }
}

private fun getStartDestination(): String {
    val user = FirebaseAuth.getInstance().currentUser
    return if (user != null) Screen.Home.route
    else Screen.Authentication.route
}