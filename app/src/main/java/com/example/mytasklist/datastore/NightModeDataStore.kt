package com.example.mytasklist.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mytasklist.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

object NightModeDataStore {

    private val darkThemePreferences = stringPreferencesKey("dark_theme")

    fun editDataStore(ctx: Context, scope: CoroutineScope, isDarkTheme: String) {
        scope.launch {
            ctx.dataStore.edit { preferences ->
                preferences[darkThemePreferences] = isDarkTheme
            }
        }
    }

    fun getDataStore(ctx: Context): Flow<String> {
        return ctx.dataStore.data
            .map { preferences ->
                preferences[darkThemePreferences] ?: "1"
            }
    }
}