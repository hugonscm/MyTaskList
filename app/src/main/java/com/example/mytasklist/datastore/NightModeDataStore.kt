package com.example.mytasklist.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.mytasklist.darkThemePreferences
import com.example.mytasklist.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

fun editDataStore(ctx: Context, scope: CoroutineScope, darkTheme: Boolean) {
    scope.launch {
        ctx.dataStore.edit { preferences ->
            preferences[darkThemePreferences] = if (darkTheme) "1" else "0"
        }
    }
}

fun getDataStore(ctx: Context): Flow<String> {
    return ctx.dataStore.data
        .map { preferences ->
            preferences[darkThemePreferences] ?: "0"
        }
}