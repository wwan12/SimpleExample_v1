package com.hq.tool

import android.app.Application

class BaseApp:Application() {
    override fun onCreate() {
        super.onCreate()
        NeverCrash.getInstance().register(this)
        NeverCrash.getInstance().setMainCrashHandler { t, e ->
            e.printStackTrace()
        }
        NeverCrash.getInstance().setUncaughtCrashHandler { t, e ->
            e.printStackTrace()
        }
    }
}