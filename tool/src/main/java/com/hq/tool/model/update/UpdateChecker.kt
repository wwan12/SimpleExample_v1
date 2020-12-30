package com.hq.tool.model.update

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.hq.tool.widget.openAnyViewWindowCenter

object UpdateChecker {

    fun checkForDialog(context: Context?,upMsg:String,updateUrl: String,force:Boolean,upEnter:(b:Boolean)->Unit,upproCall:(i:Int)->Unit) {
        if (context != null) {
            UpdateDialog.show(context, upMsg, updateUrl,force,upEnter,upproCall)
        } else {

        }
    }

    fun checkForView(activity:Activity,updateUrl: String,rId:Int, enterId: Int,cancelId:Int,upproCall:(i:Int)->Unit,exCall:()->Unit): PopupWindow {
       val pop= activity.openAnyViewWindowCenter(
            rId,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
        pop.contentView.findViewById<View>(enterId).setOnClickListener { goToDownload(activity,updateUrl,upproCall,exCall) }
        pop.contentView.findViewById<View>(cancelId).setOnClickListener { pop.dismiss() }
        return pop
    }

    fun checkForNotification(context: Context?,upMsg:String, updateUrl: String) {
        if (context != null) {
            NotificationHelper(context).showNotification(upMsg, updateUrl)
        } else {

        }
    }

     fun goToDownload(context: Context, downloadUrl: String,upproCall:(i:Int)->Unit,exCall: () -> Unit) {
        val intent = Intent(context.applicationContext, DownloadService::class.java)
        intent.putExtra(DownloadService.DOWNLOADURL, downloadUrl)
        DownloadService.upCall=upproCall
         DownloadService.exceptionCall=exCall
        context.startService(intent)
    }

    data class UpAppData(val context: Context,val updateUrl: String)
}
