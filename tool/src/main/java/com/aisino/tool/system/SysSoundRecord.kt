package com.aisino.tool.system

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

/**
 * 文件描述：
 * 作者：Administrator
 * 创建时间：2018/9/13/013
 * 更改时间：2018/9/13/013
 * 版本号：1
 *
 */

val SOUND_REQUEST = 3000

fun Activity.openRecordSound(): Unit {
    val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
    startActivityForResult(intent, SOUND_REQUEST)
}
