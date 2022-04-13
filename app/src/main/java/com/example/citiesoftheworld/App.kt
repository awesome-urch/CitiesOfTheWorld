package com.example.citiesoftheworld

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        startKoin {
//            androidContext(this@App)
//            modules(listOf(appModule, repoModule, viewModelModule, dbModule))
//        }
    }
}
