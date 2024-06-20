package com.example.mytasklist.room

import com.example.mytasklist.model.Task
import kotlinx.coroutines.flow.Flow

class OfflineTasksRepository(private val taskDao: TaskDao) : TasksRepository {

    override fun getTasks(): Flow<List<Task>> = taskDao.getTasks()

    override fun getTask(id: Int): Flow<Task> = taskDao.getTask(id)

    override suspend fun addTask(task: Task) = taskDao.addTask(task)

    override suspend fun removeTask(task: Task) = taskDao.removeTask(task)

    override suspend fun updateTask(task: Task) = taskDao.updateTask(task)
}
