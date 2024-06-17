package com.example.mytasklist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytasklist.model.Task
import com.example.mytasklist.room.TaskDatabaseDao
import com.example.mytasklist.states.TaskState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TaskViewModel(
    private val dao: TaskDatabaseDao
) : ViewModel() {
    var state by mutableStateOf(TaskState())
        private set

    init {
        viewModelScope.launch {
            dao.getTasks().collectLatest {
                state = state.copy(
                    taskList = it
                )
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
}