package com.sustainhive.ecoconnect.di.onboarding

import android.content.Context
import com.sustainhive.ecoconnect.data.onboarding.OnboardingDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingDataStoreModule {

    @Provides
    @Singleton
    fun provideOnboardingDataStore(
        @ApplicationContext context: Context
    ) = OnboardingDataStore(context = context)
}