package com.example.mytasklist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytasklist.model.Task
import com.example.mytasklist.room.TaskDatabaseDao
import com.example.mytasklist.states.TaskState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(
    private val dao: TaskDatabaseDao
) : ViewModel() {

    private val _taskState = MutableStateFlow(TaskState())
    val taskState: StateFlow<TaskState> = _taskState.asStateFlow()

    init {
        viewModelScope.launch {
            dao.getTasks().collectLatest {
                _taskState.update { currentState ->
                    currentState.copy(
                        taskList = it
                    )
                }
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