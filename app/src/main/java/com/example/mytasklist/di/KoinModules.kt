package com.example.mytasklist.di

import android.content.Context
import androidx.room.Room
import com.example.mytasklist.room.OfflineTasksRepository
import com.example.mytasklist.room.TaskDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.example.mytasklist.viewmodel.TaskViewModel
import com.example.mytasklist.viewmodel.EditTaskViewModel
import com.example.mytasklist.viewmodel.AddTaskViewModel
import com.example.mytasklist.viewmodel.ThemeViewModel
import com.example.mytasklist.room.TasksRepository

val appModule = module {
    viewModelOf(::TaskViewModel)
    viewModelOf(::EditTaskViewModel)
    viewModelOf(::AddTaskViewModel)
    viewModelOf(::ThemeViewModel)
}

val dbModule = module {
    single { provideDatabase(androidContext()) }
    single { get<TaskDatabase>().taskDao() }
    single<TasksRepository> { OfflineTasksRepository(taskDao = get()) }
}

fun provideDatabase(androidContext: Context): TaskDatabase {
    return Room.databaseBuilder(
        androidContext,
        TaskDatabase::class.java, "db_task"
    )
        .fallbackToDestructiveMigration()
        .build()
}