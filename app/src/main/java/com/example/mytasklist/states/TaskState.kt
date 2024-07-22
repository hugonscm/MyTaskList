package com.example.mytasklist.states

import com.example.mytasklist.model.Task

data class TaskState(
    val task: Task = Task(title = "", details = "")
)