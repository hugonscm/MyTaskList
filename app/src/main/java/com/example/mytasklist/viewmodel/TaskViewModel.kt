package com.example.mytasklist.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytasklist.datastore.NightModeDataStore
import com.example.mytasklist.model.Task
import com.example.mytasklist.room.TaskDatabaseDao
import com.example.mytasklist.states.TaskState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class TaskViewModel(
    private val dao: TaskDatabaseDao,
    ctx: Context,
) : ViewModel() {
    var state by mutableStateOf(TaskState())
        private set

    init {
        viewModelScope.launch {
            combine(
                dao.getTasks(),
                NightModeDataStore.getDataStore(ctx)
            ) { tasks, isDarkTheme ->
                TaskState(
                    taskList = tasks,
                    isDarkTheme = isDarkTheme
                )
            }.collectLatest { newState ->
                state = newState
            }
        }
    }

    fun addTask(task: Task) = viewModelScope.launch {
        dao.addTask(task = task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        dao.updateTask(task = task)
    }

    fun removeTask(task: Task) = viewModelScope.launch {
        dao.removeTask(task = task)
    }

    fun darkThemeChange(ctx: Context, scope: CoroutineScope, darkTheme: Boolean) {
        NightModeDataStore.editDataStore(ctx, scope, darkTheme)
    }
}