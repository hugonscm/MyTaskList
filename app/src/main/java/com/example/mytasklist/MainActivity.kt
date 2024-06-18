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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.mytasklist.navigation.NavManager
import com.example.mytasklist.room.TaskDatabase
import com.example.mytasklist.theme.ThemeSwitcherTheme
import com.example.mytasklist.viewmodel.ThemeViewModel
import com.example.mytasklist.viewmodel.ThemeViewModelFactory
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dark_theme_datastore")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database =
            Room.databaseBuilder(this, TaskDatabase::class.java, "db_task").build()
        val dao = database.taskDao()

        val splashScreen = installSplashScreen()

        setContent {
            val ctx = LocalContext.current
            val scope = rememberCoroutineScope()

            val themeViewModel: ThemeViewModel = viewModel(factory = ThemeViewModelFactory(ctx))
            val themeViewModelState by themeViewModel.themeState.collectAsState()

            val keepSplashScreenOnScreen = !themeViewModelState.isThemeLoaded
            splashScreen.setKeepOnScreenCondition { keepSplashScreenOnScreen }

            // 0 = darktheme  1 = lighttheme
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
                    },
                    dao
                )
            }
        }
    }
}