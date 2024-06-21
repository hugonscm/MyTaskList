package com.example.mytasklist.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mytasklist.TaskApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TaskViewModel(taskApplication().container.tasksRepository)
        }
        initializer {
            ThemeViewModel(taskApplication().applicationContext)
        }
    }
}

fun CreationExtras.taskApplication(): TaskApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskApplication)
