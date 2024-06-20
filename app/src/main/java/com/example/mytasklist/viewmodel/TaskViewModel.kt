package com.example.mytasklist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytasklist.model.Task
import com.example.mytasklist.room.TasksRepository
import com.example.mytasklist.states.TaskState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _taskState = MutableStateFlow(TaskState())
    val taskState: StateFlow<TaskState> = _taskState.asStateFlow()

    init {
        viewModelScope.launch {
            tasksRepository.getTasks().collectLatest {
                _taskState.update { currentState ->
                    currentState.copy(
                        taskList = it
                    )
                }
            }
        }
    }

    suspend fun addTask(task: Task) {
        tasksRepository.addTask(task)
    }

    suspend fun updateTask(task: Task) {
        tasksRepository.updateTask(task = task)
    }

    suspend fun removeTask(task: Task) {
        tasksRepository.removeTask(task = task)
    }
}