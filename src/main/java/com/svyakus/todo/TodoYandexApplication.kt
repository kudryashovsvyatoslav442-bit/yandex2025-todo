package com.svyakus.todo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoYandexApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}