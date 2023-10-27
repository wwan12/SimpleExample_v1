package com.hq.generalsecurity.set

import android.app.Application

object Storage {

    var pageUpdateTime=0L

    var downloadPath=""

    var cachePath=""

    var unZipPwd=""

    fun defPath(application: Application): Unit {
        downloadPath=application.cacheDir.absolutePath
        cachePath=application.filesDir.absolutePath
    }
}