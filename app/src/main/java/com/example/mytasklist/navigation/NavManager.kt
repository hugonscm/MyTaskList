package com.example.mytasklist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mytasklist.view.AddTaskView
import com.example.mytasklist.view.EditTaskView
import com.example.mytasklist.view.HomeView

@Composable
fun NavManager(darkTheme: Boolean, onThemeUpdated: () -> Unit) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "homeView") {
        composable("homeView") {
            HomeView(navController, darkTheme, onThemeUpdated)
        }

        composable("addTaskView") {
            AddTaskView(navController)
        }

        composable(
            "editTaskView/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
            )
        ) {
            EditTaskView(
                navController,
                it.arguments!!.getInt("id"),
            )
        }
    }
}