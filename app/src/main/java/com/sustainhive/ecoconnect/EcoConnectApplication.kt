package com.sustainhive.ecoconnect

import android.app.Application
import com.google.android.recaptcha.Recaptcha
import com.google.android.recaptcha.RecaptchaClient
import com.google.android.recaptcha.RecaptchaException
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import javax.inject.Singleton

@HiltAndroidApp
class EcoConnectApplication: Application() {

    @Singleton
    lateinit var recaptchaClientDeferred: Deferred<RecaptchaClient>
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        initializeRecaptchaClient()
    }

    private fun initializeRecaptchaClient() {
        recaptchaClientDeferred = applicationScope.async {
            try {
                Recaptcha.fetchClient(this@EcoConnectApplication, "6Lc_A24qAAAAABndVNANG8gY1T1lF40ilKYZuTyW")
            } catch (e: RecaptchaException) {
                // Handle errors appropriately, or rethrow
                throw e
            }
        }
    }
}