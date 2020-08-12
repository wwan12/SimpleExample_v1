package com.aisino.tool.model.update

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent

import com.aisino.tool.R

internal object UpdateDialog {

    fun show(context: Context, content: String, downloadUrl: String,force:Boolean,upEnter:(b:Boolean)->Unit,upproCall:(i:Int)->Unit) {
        if (isContextValid(context)) {
            if (force){
                val ar= AlertDialog.Builder(context)
                    .setTitle(R.string.android_auto_update_dialog_title)
                    .setMessage(content)
                    .setPositiveButton(R.string.android_auto_update_dialog_btn_download) { dialog, id ->
                        upEnter(true)
                        goToDownload(context, downloadUrl,upproCall) }
                    .setCancelable(false)
                    .show()
            }else{
                val ar= AlertDialog.Builder(context)
                    .setTitle(R.string.android_auto_update_dialog_title)
                    .setMessage(content)
                    .setPositiveButton(R.string.android_auto_update_dialog_btn_download) { dialog, id ->
                        upEnter(true)
                        goToDownload(context, downloadUrl,upproCall) }
                    .setNegativeButton(R.string.android_auto_update_dialog_btn_cancel) { dialog, id -> upEnter(false) }
                    .setCancelable(false)
                    .show()
            }
        }
    }

    private fun isContextValid(context: Context): Boolean {
        return context is Activity && !context.isFinishing
    }


    private fun goToDownload(context: Context, downloadUrl: String,upproCall:(i:Int)->Unit) {
        val intent = Intent(context.applicationContext, DownloadService::class.java)
        intent.putExtra(DownloadService.DOWNLOADURL, downloadUrl)
        DownloadService.upCall=upproCall
        context.startService(intent)
    }
}
