package com.sustainhive.ecoconnect.di.miscellaneous

import android.app.Application
import com.google.android.recaptcha.RecaptchaClient
import com.sustainhive.ecoconnect.EcoConnectApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecaptchaModule {

    @Provides
    @Singleton
    fun provideRecaptchaClient(application: Application): RecaptchaClient {
        return runBlocking { (application as EcoConnectApplication).recaptchaClientDeferred.await() }
    }
}