package com.aisino.tool.system

import android.app.Activity

import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by 浩琦 on 2017/7/10.
 * EasyPermissions
 */


fun Activity.signPermissions(perm: Array<String>) {
    if (EasyPermissions.hasPermissions(this, *perm)) {
        // Already have permission, do the thing
        // ...
    } else {
        // Do not have permissions, request them now
        EasyPermissions.requestPermissions(this, "运行需要权限，拒绝可能导致有些功能无法正常运行", 0, *perm)
    }
}





