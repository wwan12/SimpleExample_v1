package com.aisino.tool.model.update

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent

import com.aisino.tool.R

internal object UpdateDialog {


    fun show(context: Context, content: String, downloadUrl: String) {
        if (isContextValid(context)) {
            AlertDialog.Builder(context)
                    .setTitle(R.string.android_auto_update_dialog_title)
                    .setMessage(content)
                    .setPositiveButton(R.string.android_auto_update_dialog_btn_download) { dialog, id -> goToDownload(context, downloadUrl) }
                    .setNegativeButton(R.string.android_auto_update_dialog_btn_cancel) { dialog, id -> }
                    .setCancelable(false)
                    .show()
        }
    }

    private fun isContextValid(context: Context): Boolean {
        return context is Activity && !context.isFinishing
    }


    private fun goToDownload(context: Context, downloadUrl: String) {
        val intent = Intent(context.applicationContext, DownloadService::class.java)
        intent.putExtra(DownloadService.DOWNLOADURL, downloadUrl)
        context.startService(intent)
    }
}
