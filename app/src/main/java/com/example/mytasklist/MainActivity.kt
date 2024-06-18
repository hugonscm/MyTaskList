package com.example.mytasklist

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.mytasklist.navigation.NavManager
import com.example.mytasklist.room.TaskDatabase
import com.example.mytasklist.theme.ThemeSwitcherTheme
import com.example.mytasklist.view.LoadingView
import com.example.mytasklist.viewmodel.TaskViewModel
import com.example.mytasklist.viewmodel.ThemeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//usado para salvar o id do usuario logado, precisa ser definido no level mais alto do projeto
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dark_theme_datastore")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database =
            Room.databaseBuilder(this, TaskDatabase::class.java, "db_task").build()
        val dao = database.taskDao()

        installSplashScreen()
        setContent {

            val ctx = LocalContext.current
            val scope = rememberCoroutineScope()
            val taskViewModel = TaskViewModel(dao)
            val themeViewModel = ThemeViewModel(ctx)

            // 0 = dark  1 = light
            val isDarkTheme by remember {
                derivedStateOf {
                    themeViewModel.state.isDarkTheme == "0"
                }
            }

            // manter os icons da navBar visiveis ao trocar cor do tema
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars =
                !isDarkTheme
            // manter os icons da statusBar visiveis ao trocar cor do tema
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                !isDarkTheme

            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                isLoading = true
                delay(300)
                isLoading = false
            }

            ThemeSwitcherTheme(darkTheme = isDarkTheme) {
                if (isLoading) {
                    LoadingView()
                } else {
                    NavManager(
                        taskViewModel = taskViewModel,
                        darkTheme = isDarkTheme,
                        onThemeUpdated = {
                            scope.launch {
                                themeViewModel.darkThemeChange(ctx, scope, !isDarkTheme)
                                isLoading = true
                                delay(500)
                                isLoading = false
                            }
                        }
                    )
                }
            }
        }
    }
}