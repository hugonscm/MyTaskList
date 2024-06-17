package com.example.mytasklist.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytasklist.datastore.NightModeDataStore
import com.example.mytasklist.states.NightModeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ThemeViewModel(
    ctx: Context
) : ViewModel() {
    var state by mutableStateOf(NightModeState())
        private set

    init {
        viewModelScope.launch {
            NightModeDataStore.getDataStore(ctx).collectLatest {
                state = state.copy(
                    isDarkTheme = it
                )
            }
        }
    }

    fun darkThemeChange(ctx: Context, scope: CoroutineScope, darkTheme: Boolean) {
        NightModeDataStore.editDataStore(ctx, scope, darkTheme)
    }
}
