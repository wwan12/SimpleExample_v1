package com.aisino.tool.service

import android.app.Activity
import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v7.widget.DialogTitle


/**
 * Created by lenovo on 2017/12/11.
 */
fun Activity.showNotifictionIcon(id:Int,icon:Int,title: String,text :String,tick:String) {
    val builder = NotificationCompat.Builder(this)
    val intent = Intent(this, this::class.java)//将要跳转的界面
    builder.setAutoCancel(true)//点击后消失
    builder.setSmallIcon(icon)//设置通知栏消息标题的头像
    builder.setDefaults(NotificationCompat.DEFAULT_SOUND)//设置通知铃声
    builder.setTicker(tick)
    builder.setContentText(text)//通知内容
    builder.setContentTitle(title)
    //利用PendingIntent来包装我们的intent对象,使其延迟跳转
    val intentPend = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    builder.setContentIntent(intentPend)
    val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.notify(id, builder.build())
}