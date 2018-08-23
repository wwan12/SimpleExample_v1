package com.aisino.tool.model.update

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.util.Log

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class DownloadService : IntentService("DownloadService") {

    override fun onHandleIntent(intent: Intent) {

        val notificationHelper = NotificationHelper(this)
        val urlStr = intent.getStringExtra(DOWNLOADURL)
        var ins: InputStream? = null
        var out: FileOutputStream? = null
        val url = URL(urlStr)
        val urlConnection = url.openConnection() as HttpURLConnection

        urlConnection.requestMethod = "GET"
        urlConnection.doOutput = false
        urlConnection.connectTimeout = 10 * 1000
        urlConnection.readTimeout = 10 * 1000
        urlConnection.setRequestProperty("Connection", "Keep-Alive")
        urlConnection.setRequestProperty("Charset", "UTF-8")
        urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate")

        urlConnection.connect()
        val bytetotal = urlConnection.contentLength.toLong()
        var bytesum: Long = 0
        var byteread = 0
        ins = urlConnection.inputStream
        val dir = cacheDir
        val apkName = urlStr.substring(urlStr.lastIndexOf("/") + 1, urlStr.length)
        val apkFile = File(dir, apkName)
        out = FileOutputStream(apkFile)
        val buffer = ByteArray(BUFFER_SIZE)

        var oldProgress = 0
        byteread = ins.read(buffer)
        while (byteread != -1) {
            bytesum += byteread.toLong()
            out.write(buffer, 0, byteread)

            val progress = (bytesum * 100L / bytetotal).toInt()
            // 如果进度与之前进度相等，则不更新，如果更新太频繁，否则会造成界面卡顿
            if (progress != oldProgress) {
                notificationHelper.updateProgress(progress)
            }
            oldProgress = progress
            byteread = ins.read(buffer)
        }
        // 下载完成
        installAPk(this, apkFile)
        notificationHelper.cancel()
        try {
            out?.close()
            ins?.close()
        } catch (ignored: IOException) {
            ignored.printStackTrace()
        }


    }

    fun installAPk(context: Context, apkFile: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            val uri = FileProvider.getUriForFile(context,  "com.aisino.tool.fileProvider", apkFile)

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
        } else {
            try {
                val command = arrayOf(apkFile.toString())
                val builder = ProcessBuilder(*command)
                builder.start()
            } catch (ignored: IOException) {
            }
            val uri = Uri.fromFile(apkFile)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
        }
        context.startActivity(intent)
    }

    companion object {
        private val BUFFER_SIZE = 10 * 1024 // 8k ~ 32K
        val DOWNLOADURL = "DOWNLOADURL"
    }


}
