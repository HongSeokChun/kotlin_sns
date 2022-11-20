package com.android.example.hongseokchun

import android.app.Application

class MyApplication : Application() {
    companion object {
        lateinit var prefs: MySharedPreferences
    }

    override fun onCreate() {
        prefs = MySharedPreferences(applicationContext)
        super.onCreate()
    }
}