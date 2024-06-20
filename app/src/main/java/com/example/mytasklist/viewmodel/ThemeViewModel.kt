package com.example.mytasklist.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytasklist.datastore.NightModeDataStore
import com.example.mytasklist.states.NightModeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThemeViewModel(
    ctx: Context
) : ViewModel() {

    private val _thmemeState = MutableStateFlow(NightModeState())
    val themeState: StateFlow<NightModeState> = _thmemeState.asStateFlow()

    init {
        viewModelScope.launch {
            NightModeDataStore.getDataStore(ctx).collectLatest {
                _thmemeState.update { currentState ->
                    currentState.copy(
                        isDarkTheme = it,
                        isThemeLoaded = true
                    )
                }
            }
        }
    }

    fun darkThemeChange(ctx: Context, scope: CoroutineScope, isDarkTheme: String) {
        NightModeDataStore.editDataStore(ctx, scope, isDarkTheme)
    }
}
