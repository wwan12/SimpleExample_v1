package com.hq.tool

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hq.tool.ani.LoadingDialog


/**
 * Created by lenovo on 2017/11/14.
 * 开发用
 */
private var DEBUG = true


fun String.log(tag:String="tag"): Unit {

        Log.i(tag, this)

}
fun String.loge(tag:String="tag"): Unit {

        Log.e(tag, this)

}

fun String.toast(context: Context): Unit {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun Activity.onLoad(): LoadingDialog? {
    try {
        val subLog = LoadingDialog(this)
        subLog.show()
//    val load = AlertDialog.Builder(this)
//    val subLog = load.show() as LoadingDialog
        subLog.setCancelable(false)
        return subLog
    }catch (e:Exception){
        e.printStackTrace()
        return null
    }

}


fun Activity.onLoadPro(rid:Int): View {
    var success: AlertDialog? = null
    val load = AlertDialog.Builder(this)
    val log = layoutInflater.inflate(rid, null, false)

    load.setView(log)
    success = load.show()
    success.setCancelable(false)
    return log
}

fun String.dialog(context: Context): Unit {
    val load = AlertDialog.Builder(context)
            .setMessage(this)
            .setPositiveButton("确认") { dialog, id -> }
            .show()
    //   success.setCancelable(true)
}

fun String.save(activity: AppCompatActivity,key:String): Unit {
    activity.getSharedPreferences("activity",AppCompatActivity.MODE_PRIVATE).edit().putString(key,this).apply()
}

fun String.load(activity: AppCompatActivity): String {
    return activity.getSharedPreferences("activity", AppCompatActivity.MODE_PRIVATE).getString(this,"")
}

fun String.promptError(describe: String): Unit {
        Log.e("promptError", "File\t" + getFileName() + "\n"
                + "Class\t" + getClassName() + "\n"
                + "Method\t" + getMethodName() + "\n" + "Line\t" + getLineNumber() + "\n$this\n$describe")
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

class If{
    fun I(b: Boolean): If {
        return this
    }
    fun S(e:()->Unit): If {
        return this
    }
    fun E(e:()->Unit): If {
        return this
    }
}

