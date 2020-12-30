package com.hq.tool.cache

import androidx.appcompat.app.AppCompatActivity

fun String.save(activity: AppCompatActivity, key:String): Unit {
    activity.getSharedPreferences("activity", AppCompatActivity.MODE_PRIVATE).edit().putString(key,this).apply()
}

fun String.load(activity: AppCompatActivity): String {
    return activity.getSharedPreferences("activity", AppCompatActivity.MODE_PRIVATE).getString(this,"").toString()
}