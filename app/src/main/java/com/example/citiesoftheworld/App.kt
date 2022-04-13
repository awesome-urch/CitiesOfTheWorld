package com.example.citiesoftheworld

import android.app.Application
import com.example.citiesoftheworld.di.appModule
import com.example.citiesoftheworld.di.dbModule
import com.example.citiesoftheworld.di.repoModule
import com.example.citiesoftheworld.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

//        startKoin {
//            androidContext(this@App)
//        }

        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, repoModule, viewModelModule, dbModule))
        }
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}
