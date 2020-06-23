package com.aisino.tool.model.update

import android.content.Context
import android.content.Intent

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
    fun downloadApk(context: Context, downloadUrl: String) {
        val intent = Intent(context.applicationContext, DownloadService::class.java)
        intent.putExtra(DownloadService.DOWNLOADURL, downloadUrl)
        context.startService(intent)
    }
}
