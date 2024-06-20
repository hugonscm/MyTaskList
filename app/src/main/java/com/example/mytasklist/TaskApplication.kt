package com.example.mytasklist

import android.app.Application
import com.example.mytasklist.room.AppContainer
import com.example.mytasklist.room.AppDataContainer


class TaskApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
