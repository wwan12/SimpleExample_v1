package com.aisino.tool.system

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by 浩琦 on 2017/7/10.
 * EasyPermissions
 */


fun Activity.signPermissions(p:String="",allSign: () -> Unit={}) {
    var perms:Array<String>
    if (p.equals("")){
         perms= this.getAllPermissions()
    }else{
        perms= emptyArray()
        perms.plus(p)

    }
    if (EasyPermissions.hasPermissions(this, *perms)) {
        // 权限全部通过
        allSign()
    } else {
        // 有权限被拒绝
        EasyPermissions.requestPermissions(this, "运行需要权限，拒绝可能导致有些功能无法正常运行", 0, *perms)
    }
}

fun Activity.checkPermission(perm: String,go: () -> Unit,fail: () -> Unit): Unit {
        if (ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED) {
            go()
        }else{
            fail()
        }
}







