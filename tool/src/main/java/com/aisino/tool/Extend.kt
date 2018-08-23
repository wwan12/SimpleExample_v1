package com.aisino.tool

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Created by lenovo on 2017/11/14.
 * 开发用
 */
private var DEBUG = true
fun Activity.DEBUG(boolean: Boolean): Unit {
    DEBUG=boolean
}

fun DEBUG(): Boolean {
    return DEBUG
}

fun String.log(tag:String="tag"): Unit {
    if (DEBUG){
        Log.i(tag, this)
    }
}

fun String.toast(context: Context): Unit {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun String.promptError(describe: String): Unit {
    if (DEBUG){
        Log.e("promptError", "File\t" + getFileName() + "\n"
                + "Class\t" + getClassName() + "\n"
                + "Method\t" + getMethodName() + "\n" + "Line\t" + getLineNumber() + "\n$this\n$describe")
    }
}


private fun getFileName(): String {
    return Thread.currentThread().stackTrace[2].fileName
}

private fun getClassName(): String {
    return Thread.currentThread().stackTrace[2].className
}

private fun getMethodName(): String {
    return Thread.currentThread().stackTrace[2].methodName
}

private fun getLineNumber(): Int {
    return Thread.currentThread().stackTrace[2].lineNumber
}


