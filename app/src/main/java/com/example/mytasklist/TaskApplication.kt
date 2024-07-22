package com.example.mytasklist

import android.app.Application
import com.example.mytasklist.di.appModule
import com.example.mytasklist.di.dbModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class TaskApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TaskApplication)
            modules(appModule, dbModule)
        }
    }
}
