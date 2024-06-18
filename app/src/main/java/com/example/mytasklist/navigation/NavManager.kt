package com.example.mytasklist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mytasklist.room.TaskDatabaseDao
import com.example.mytasklist.view.AddTaskView
import com.example.mytasklist.view.EditTaskView
import com.example.mytasklist.view.HomeView

@Composable
fun NavManager(darkTheme: Boolean, onThemeUpdated: () -> Unit, dao: TaskDatabaseDao) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "homeView") {
        composable("homeView") {
            HomeView(navController, darkTheme, onThemeUpdated, dao)
        }

        composable("addTaskView") {
            AddTaskView(navController, dao)
        }

        composable("editTaskView/{id}/{title}/{details}", arguments = listOf(
            navArgument("id") { type = NavType.IntType },
            navArgument("title") { type = NavType.StringType },
            navArgument("details") { type = NavType.StringType }
        )) {
            EditTaskView(
                navController,
                it.arguments!!.getInt("id"),
                it.arguments?.getString("title").toString(),
                it.arguments?.getString("details").toString(),
                dao
            )
        }
    }
}