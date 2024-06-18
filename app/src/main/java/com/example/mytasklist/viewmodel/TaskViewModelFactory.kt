package com.example.mytasklist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytasklist.room.TaskDatabaseDao

class TaskViewModelFactory(private val dao: TaskDatabaseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}