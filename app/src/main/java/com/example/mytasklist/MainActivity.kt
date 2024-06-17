package com.example.mytasklist

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.mytasklist.viewmodel.TaskViewModel

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
            val viewModel = TaskViewModel(dao, ctx)

            // 0 = dark  1 = light
            val isDarkTheme by remember {
                derivedStateOf {
                    viewModel.state.isDarkTheme == "0"
                }
            }

            // manter os icons da navBar visiveis ao trocar cor do tema
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars =
                !isDarkTheme
            // manter os icons da statusBar visiveis ao trocar cor do tema
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                !isDarkTheme

            ThemeSwitcherTheme(darkTheme = isDarkTheme) {
                NavManager(
                    viewModel = viewModel,
                    darkTheme = isDarkTheme,
                    onThemeUpdated = {
                        viewModel.darkThemeChange(ctx, scope, !isDarkTheme)
                    }
                )
            }
        }
    }
}