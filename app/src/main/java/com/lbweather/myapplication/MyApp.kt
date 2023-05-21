package com.lbweather.myapplication

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.lbweather.myapplication.data.database.MyDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@MyApp)
            modules(listOf(dataModule))
        }

    }

    companion object {
        fun logData(any: Any?) {
            Log.i("myLogger", "$any")
        }
    }
}
