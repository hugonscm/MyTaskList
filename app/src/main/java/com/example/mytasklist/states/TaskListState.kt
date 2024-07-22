package com.example.mytasklist.states

import com.example.mytasklist.model.Task

data class TaskListState(
    val taskList: List<Task> = emptyList()
)
