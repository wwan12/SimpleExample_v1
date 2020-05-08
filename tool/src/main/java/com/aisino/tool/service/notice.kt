package com.aisino.tool.service

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat


/**
 * Created by lenovo on 2017/12/11.
 * 推送一个前台通知
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

fun Activity.Tips(pushId:Int,icon:Int,title: String,text:String,num:Int): Unit {
    val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val mBuilder = NotificationCompat.Builder(this)
    val notificationIntent = Intent(this, this::class.java)
    notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    val intent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
    mBuilder.setContentTitle(title)//设置通知栏标题
            .setContentText(text)
            .setContentIntent(intent) //设置通知栏单击意图
            .setNumber(num) //设置通知集合的数量
            .setTicker(title + ":" + text) //通知首次出现在通知栏，带上升动画效果的
            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
            .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用 defaults 属性，可以组合
            .setSmallIcon(icon)//设置通知小 ICON
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel  =  NotificationChannel("9999", title, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true) //是否在桌面icon右上角展示小红点
        channel.setLightColor(Color.RED)//小红点颜色
        channel.setShowBadge(true)
        //是否在久按桌面图标时显示此渠道的通知
        mNotificationManager.createNotificationChannel(channel); //同时，Notification.Builder需要多设置一个
        mBuilder.setChannelId("9999");
    }
    val notify = mBuilder.build()
    notify.flags = notify.flags or Notification.FLAG_AUTO_CANCEL
    mNotificationManager.notify(pushId, notify)

}