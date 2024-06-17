package com.example.mytasklist

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.mytasklist.datastore.editDataStore
import com.example.mytasklist.datastore.getDataStore
import com.example.mytasklist.navigation.NavManager
import com.example.mytasklist.room.TaskDatabase
import com.example.mytasklist.theme.MyPurple
import com.example.mytasklist.theme.MyWhite
import com.example.mytasklist.theme.ThemeSwitcherTheme
import com.example.mytasklist.viewmodel.TaskViewModel

//usado para salvar o id do usuario logado, precisa ser definido no level mais alto do projeto
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dark_theme_datastore")
val darkThemePreferences = stringPreferencesKey("dark_theme")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        setContent {

            val ctx = LocalContext.current
            val scope = rememberCoroutineScope()

            val darkThemeFlow = remember { getDataStore(ctx) }

            val isDarkTheme by darkThemeFlow.collectAsState(initial = "-2")
            var darkTheme by remember { mutableStateOf(isDarkTheme == "1") }

            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(isDarkTheme) {
                when (isDarkTheme) {
                    "-2" -> isLoading = true
                    "0" -> {
                        darkTheme = false
                        isLoading = false
                    }

                    "1" -> {
                        darkTheme = true
                        isLoading = false
                    }

                    else -> isLoading = false
                }
            }

            // manter os icons da navBar visiveis ao trocar cor do tema
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars =
                !darkTheme
            // manter os icons da statusBar visiveis ao trocar cor do tema
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                !darkTheme

            ThemeSwitcherTheme(darkTheme = darkTheme) {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MyWhite),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MyPurple)
                    }
                } else {
                    val database =
                        Room.databaseBuilder(this, TaskDatabase::class.java, "db_task").build()
                    val dao = database.taskDao()
                    val viewModel = TaskViewModel(dao)
                    NavManager(
                        viewModel = viewModel,
                        darkTheme = darkTheme,
                        onThemeUpdated = {
                            darkTheme = !darkTheme
                            editDataStore(ctx, scope, darkTheme)
                        }
                    )
                }
            }
        }
    }
}