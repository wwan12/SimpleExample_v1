package com.hq.tool.cache

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity


fun String.save(activity: AppCompatActivity,key:String): Unit {
    activity.getSharedPreferences("activity",AppCompatActivity.MODE_PRIVATE).edit().putString(key,this).apply()
}

fun String.load(activity: AppCompatActivity): String {
    return activity.getSharedPreferences("activity", AppCompatActivity.MODE_PRIVATE).getString(this,"")!!
}

fun Boolean.saveSPBoolean(activity: Context, key: String): Unit {
    activity.getSharedPreferences("activity", AppCompatActivity.MODE_PRIVATE).edit()
        .putBoolean(key, this).apply()
}

fun String.loadSPBoolean(activity: Context): Boolean {
    return activity.getSharedPreferences("activity", AppCompatActivity.MODE_PRIVATE)
        .getBoolean(this, false)
}

fun String.savePro(app: Application, key:String): Unit {
    app.getSharedPreferences("activity",AppCompatActivity.MODE_PRIVATE).edit().putString(key,this).apply()
}

fun String.loadPro(app: Application): String {
    return app.getSharedPreferences("activity", AppCompatActivity.MODE_PRIVATE).getString(this,"")!!
}
