package com.hq.tool

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hq.tool.ani.LoadingDialog


///第一步：AndroidManifest.xml文件中设置windowSoftInputMode属性为“adjustResize”（可设置在对应页面的Activity中，亦可设置在application节点中）
//第二步：在根节点设置fitsSystemWindows属性为true。（ScrollView不要放在根节点）但是fitsSystemWindows属性会使得该节点强制PaddingTop一个statusBar高度，并且让该节点其他padding都失效。因此会导致toolbar上会有一个statusBar高度的空白区域，因此要设置根节点背景颜色和toolbar一致即可。
//第三步：到第二步结束问题基本就算被解决了，但是你会发现Toolbar会增高一个statusBar高度，不美观，因此在Java页面初始化toolbar时为toolbar添加一个负的Margins


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
    try {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }catch (e:Exception){
        e.printStackTrace()
    }

}

fun Activity.onLoad(): LoadingDialog? {
    return try {
        val subLog = LoadingDialog(this)
        subLog.show()
//    val load = AlertDialog.Builder(this)
//    val subLog = load.show() as LoadingDialog
        subLog.setCancelable(false)
        subLog
    }catch (e:Exception){
        e.printStackTrace()
        null
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

fun Int.dialog(context: Activity,enterId:Int): AlertDialog {
    var success: AlertDialog? = null
    val load = AlertDialog.Builder(context)
    var log: View = context.layoutInflater.inflate(this, null, false)
    load.setView(log)
    success = load.show()
    success?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    return success
}

fun Dialog.noCrashDismiss(): Unit {
    try {
        if (this.context is Activity){
            if (!(this.context as Activity).isDestroyed){
                this.dismiss()
            }
        }else{
            this.dismiss()
        }
    }catch (e:Exception){
        e.printStackTrace()
    }
}

fun Dialog.noCrashShow(): Unit {
    try {
        if (this.context is Activity){
            if (!(this.context as Activity).isDestroyed){
                this.show()
            }
        }else{
            this.show()
        }
    }catch (e:Exception){
        e.printStackTrace()
    }
}

fun String.save(activity: AppCompatActivity,key:String): Unit {
    activity.getSharedPreferences("activity",AppCompatActivity.MODE_PRIVATE).edit().putString(key,this).apply()
}

fun String.load(activity: AppCompatActivity): String {
    return activity.getSharedPreferences("activity", AppCompatActivity.MODE_PRIVATE).getString(this,"")!!
}
fun String.savePro(app: Application,key:String): Unit {
    app.getSharedPreferences("activity",AppCompatActivity.MODE_PRIVATE).edit().putString(key,this).apply()
}

fun String.loadPro(app: Application): String {
    return app.getSharedPreferences("activity", AppCompatActivity.MODE_PRIVATE).getString(this,"")!!
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

