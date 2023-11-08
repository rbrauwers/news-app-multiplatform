package com.rbrauwers.newsapp.multiplatform

import android.app.Application
import di.appModule
import org.koin.core.context.startKoin

class NewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }

}