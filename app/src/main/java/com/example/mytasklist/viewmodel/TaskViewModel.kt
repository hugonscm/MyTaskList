package com.example.mytasklist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytasklist.model.Task
import com.example.mytasklist.room.TasksRepository
import com.example.mytasklist.states.TaskListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _taskListState = MutableStateFlow(TaskListState())
    val taskListState: StateFlow<TaskListState> = _taskListState.asStateFlow()

    init {
        viewModelScope.launch {
            tasksRepository.getTasks().collectLatest {
                _taskListState.update { currentState ->
                    currentState.copy(
                        taskList = it
                    )
                }
            }
        }
    }

    suspend fun removeTask(task: Task) {
        tasksRepository.removeTask(task = task)
    }
}