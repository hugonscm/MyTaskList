package com.example.mytasklist.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytasklist.model.Task
import com.example.mytasklist.room.TasksRepository
import com.example.mytasklist.states.EditTaskState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditTaskViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository,
) : ViewModel() {

    private val _editTaskState = MutableStateFlow(EditTaskState())
    val editTaskState: StateFlow<EditTaskState> = _editTaskState.asStateFlow()

    private val taskId: Int = savedStateHandle.get<Int>("id") ?: -1

    init {
        viewModelScope.launch {
            tasksRepository.getTask(taskId).collect {
                _editTaskState.update { currentState ->
                    currentState.copy(
                        task = it
                    )
                }
            }

        }
    }

    fun setTittle(titlte: String) {
        _editTaskState.update { currentState ->
            currentState.copy(
                task = currentState.task.copy(title = titlte)
            )
        }
    }

    fun setDetails(details: String) {
        _editTaskState.update { currentState ->
            currentState.copy(
                task = currentState.task.copy(details = details)
            )
        }
    }

    suspend fun updateTask(task: Task) {
        tasksRepository.updateTask(task = task)
    }
}