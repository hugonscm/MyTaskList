package com.example.mytasklist.states

import com.example.mytasklist.model.Task

data class TaskState(
    val taskList: List<Task> = emptyList()
)
