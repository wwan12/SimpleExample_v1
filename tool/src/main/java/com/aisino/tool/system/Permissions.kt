package com.aisino.tool.system

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by 浩琦 on 2017/7/10.
 * EasyPermissions
 */


fun Activity.signPermissions(perms: Array<String>) {
    if (EasyPermissions.hasPermissions(this, *perms)) {
        // Already have permission, do the thing
        // ...
    } else {
        // Do not have permissions, request them now
        EasyPermissions.requestPermissions(this, "运行需要权限，拒绝可能导致有些功能无法正常运行", 0, *perms)
    }
}

fun Activity.checkPermission(perms: Array<String>): Unit {
    for (perm in perms) {
        if (ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED) {

        }else{
            EasyPermissions.requestPermissions(this, "运行需要权限，拒绝可能导致有些功能无法正常运行", 0, *perms)
        }
    }
}







