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
import com.example.mytasklist.viewmodel.TaskViewModel

@Composable
fun NavManager(viewModel: TaskViewModel, darkTheme: Boolean, onThemeUpdated: () -> Unit) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "homeView" ){
        composable("homeView"){
            HomeView(navController, viewModel, darkTheme, onThemeUpdated)
        }

        composable("addTaskView"){
            AddTaskView(navController, viewModel)
        }

        composable("editTaskView/{id}/{title}/{details}", arguments = listOf(
            navArgument("id") {type = NavType.IntType},
            navArgument("title") {type = NavType.StringType},
            navArgument("details") {type = NavType.StringType}
        )){
            EditTaskView(navController, viewModel,
                it.arguments!!.getInt("id"),
                it.arguments?.getString("title").toString(),
                it.arguments?.getString("details").toString()
            )
        }
    }
}