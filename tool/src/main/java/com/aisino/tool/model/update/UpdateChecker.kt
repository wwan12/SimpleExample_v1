package com.aisino.tool.model.update

import android.content.Context

object UpdateChecker {

    fun checkForDialog(context: Context?,upMsg:String,updateUrl: String,force:Boolean,upEnter:()->Unit={}) {
        if (context != null) {
            UpdateDialog.show(context, upMsg, updateUrl,force,upEnter)
        } else {

        }
    }


    fun checkForNotification(context: Context?,upMsg:String, updateUrl: String) {
        if (context != null) {
            NotificationHelper(context).showNotification(upMsg, updateUrl)
        } else {

        }
    }
}
