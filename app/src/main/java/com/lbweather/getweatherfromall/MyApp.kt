package com.lbweather.getweatherfromall

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.lbweather.getweatherfromall.data.dataDI
import com.lbweather.getweatherfromall.domain.domainDI
import com.lbweather.getweatherfromall.presentation.presentationDI
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this) {}

        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@MyApp)
            modules(listOf(domainDI, dataDI, presentationDI))
        }

    }

    companion object {
        fun logData(any: Any?) {
            Log.i("myLogger", "$any")
        }
    }
}
