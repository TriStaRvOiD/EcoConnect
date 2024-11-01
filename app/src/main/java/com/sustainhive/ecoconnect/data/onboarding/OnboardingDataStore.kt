package com.sustainhive.ecoconnect.data.onboarding

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding")

class OnboardingDataStore(private val context: Context) {

    private val onboardingKey = booleanPreferencesKey("onboarding_complete")

    val isOnboardingComplete: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[onboardingKey] ?: false
        }

    suspend fun setOnboardingComplete(isComplete: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[onboardingKey] = isComplete
        }
    }
}