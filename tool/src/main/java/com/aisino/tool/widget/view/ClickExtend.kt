package com.aisino.tool.widget.view

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.aisino.tool.ani.LoadingDialog

private val controllerViews= mutableMapOf<Int,Long>()

private val MIN_DELAY_TIME=1000

fun Activity.onLoad(): LoadingDialog {
    val subLog = LoadingDialog(this)
    subLog.show()
//    val load = AlertDialog.Builder(this)
//    val subLog = load.show() as LoadingDialog
    subLog.setCancelable(false)
    return subLog
}

fun Fragment.onLoad(): LoadingDialog {
    val subLog = LoadingDialog(context)
    subLog.show()
//    val load = AlertDialog.Builder(this)
//    val subLog = load.show() as LoadingDialog
    subLog.setCancelable(false)
    return subLog
}

fun View.setOnNotFastClick(click:(view: View)->Unit){
    this.setOnClickListener {
        if (controllerViews.containsKey(this.id)){
            var flag = true
            val currentClickTime = System.currentTimeMillis()
            if (currentClickTime - controllerViews[this.id]!! >= MIN_DELAY_TIME) {
                flag = false
                controllerViews.put(this.id,currentClickTime)
            }
            if (!flag){
                click(this)
            }
        }else{
            controllerViews.put(this.id,System.currentTimeMillis())
            click(this)
        }

    }
}