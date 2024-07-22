package com.example.mytasklist

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mytasklist.navigation.NavManager
import com.example.mytasklist.theme.ThemeSwitcherTheme
import com.example.mytasklist.viewmodel.ThemeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dark_theme_datastore")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        setContent {
            val ctx = LocalContext.current
            val scope = rememberCoroutineScope()

            val themeViewModel: ThemeViewModel = koinViewModel<ThemeViewModel>()
            val themeViewModelState by themeViewModel.themeState.collectAsState()

            val keepSplashScreenOnScreen = !themeViewModelState.isThemeLoaded
            splashScreen.setKeepOnScreenCondition { keepSplashScreenOnScreen }

            // 0 = darkTheme  1 = lightTheme
            val isDarkTheme =
                themeViewModelState.isThemeLoaded && themeViewModelState.isDarkTheme == "0"

            // manter os icons da navBar visiveis ao trocar cor do tema
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars =
                !isDarkTheme
            // manter os icons da statusBar visiveis ao trocar cor do tema
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                !isDarkTheme

            ThemeSwitcherTheme(darkTheme = isDarkTheme) {
                NavManager(
                    darkTheme = isDarkTheme,
                    onThemeUpdated = {
                        scope.launch {
                            themeViewModel.darkThemeChange(
                                ctx,
                                scope,
                                if (isDarkTheme) "1" else "0"
                            )
                        }
                    }
                )
            }
        }
    }
}