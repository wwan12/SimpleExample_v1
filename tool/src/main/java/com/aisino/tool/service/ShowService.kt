package com.aisino.tool.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import com.aisino.tool.R

/**
 * Created by lenovo on 2017/12/11.
 */
class ShowService(run: () -> Unit, text: String, icon: Int,layoutId:Int) : Service() {
    var _run: () -> Unit = {}
    var _text: String = ""
    var _icon: Int = 0
    var _layoutId = 0
    lateinit var _remoteViews:RemoteViews
    override fun onCreate() {
        super.onCreate()
    }

    init {
        _run = run
        _text = text
        _icon = icon
        _layoutId=layoutId
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification:Notification
        if (_layoutId == 0) {
            notification = Notification.Builder(this.getApplicationContext())
                    .setContentText(_text)
                    .setSmallIcon(_icon)
                    .setWhen(System.currentTimeMillis())
                    .build();
        }else{
            notification=showView(_layoutId)
        }

        // 参数一：唯一的通知标识；参数二：通知消息。
        startForeground(startId, notification);// 开始前台服务

        return super.onStartCommand(intent, flags, startId)

    }

    override fun onBind(intent: Intent?): IBinder? {
        if (_run != null) {
            _run()
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知

    }

    fun createClick(code: String, request: Int,id:Int): Unit {
        val intentPlay = Intent(code);// 指定操作意图--设置对应的行为ACTION
        val pIntentPlay = PendingIntent.getBroadcast(this.getApplicationContext(), request, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
        _remoteViews.setOnClickPendingIntent(id, pIntentPlay);// 绑定点击事件（参数一：控件id；参数二：对应触发的PendingIntent）
    }

    private fun showView(layoutId: Int): Notification {
        _remoteViews = RemoteViews(this.getPackageName(), layoutId);// 获取remoteViews（参数一：包名；参数二：布局资源）
        val builder = Notification.Builder(this.getApplicationContext()).setContent(_remoteViews);// 设置自定义的Notification内容
        builder.setWhen(System.currentTimeMillis()).setSmallIcon(_icon).setDefaults(Notification.DEFAULT_SOUND);
        return builder.build();// 获取构建好的通知--.build()最低要求在
    }


}