package com.aisino.tool.model.update

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat

import com.aisino.tool.R

/**
 *
 * @since 2018-04-07 16:49
 */
class NotificationHelper(base: Context) : ContextWrapper(base) {

    private var manager: NotificationManager? = null


    private//设置 nofication 的图标 直接读取小米推送配置的图标
    val smallIcon: Int
        get() {
            var icon = resources.getIdentifier("mipush_small_notification", "drawable", packageName)
            if (icon == 0) {
                icon = applicationInfo.icon
            }

            return icon
        }

    private val largeIcon: Bitmap?
        get() {
            val bigIcon = resources.getIdentifier("mipush_notification", "drawable", packageName)
            return if (bigIcon != 0) {
                BitmapFactory.decodeResource(resources, bigIcon)
            } else null
        }

    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val mChannel = NotificationChannel(CHANNEL_ID, "应用更新", NotificationManager.IMPORTANCE_LOW)
            mChannel.description = "应用有新版本"
            mChannel.enableLights(true) //是否在桌面icon右上角展示小红点
            getManager().createNotificationChannel(mChannel)
        }
    }

    /**
     * Show Notification
     */
    fun showNotification(content: String, apkUrl: String) {

        val myIntent = Intent(this, DownloadService::class.java)
        myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        myIntent.putExtra(DownloadService.DOWNLOADURL, apkUrl)
        val pendingIntent = PendingIntent.getService(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = getNofity(content)
                .setContentIntent(pendingIntent)

        getManager().notify(NOTIFICATION_ID, builder.build())
    }


    fun updateProgress(progress: Int) {


        val text = this.getString(R.string.android_auto_update_download_progress, progress)

        val pendingintent = PendingIntent.getActivity(this, 0, Intent(), PendingIntent.FLAG_UPDATE_CURRENT)


        val builder = getNofity(text)
                .setProgress(100, progress, false)
                .setContentIntent(pendingintent)

        getManager().notify(NOTIFICATION_ID, builder.build())
    }

    private fun getNofity(text: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setTicker(getString(R.string.android_auto_update_notify_ticker))
                .setContentTitle("应用更新")
                .setContentText(text)
                .setSmallIcon(smallIcon)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)

    }

    fun cancel() {
        getManager().cancel(NOTIFICATION_ID)
    }

    private fun getManager(): NotificationManager {
        if (manager == null) {
            manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return manager as NotificationManager
    }

    companion object {

        private val CHANNEL_ID = "dxy_app_update"

        private val NOTIFICATION_ID = 0
    }
}
