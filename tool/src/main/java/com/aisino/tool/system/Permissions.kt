package com.aisino.tool.system

import android.app.Activity
import androidx.core.app.ActivityCompat

import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by 浩琦 on 2017/7/10.
 * EasyPermissions
 *
EasyPermissions.requestPermissions(this,
"接下来需要获取WRITE_EXTERNAL_STORAGE权限",
R.string.yes,
R.string.no,
0,
Manifest.permission.WRITE_EXTERNAL_STORAGE);
 *
 *
 *     @AfterPermissionGranted(0)
 */

/**
 * 申请全部权限
 */
fun Activity.signPermissions(allSign: () -> Unit={}) {
    var perms:Array<String> = this.getAllPermissions()
    if (EasyPermissions.hasPermissions(this, *perms)) {
        // 权限全部通过
        allSign()
    } else {
        // 有权限被拒绝
        EasyPermissions.requestPermissions(this, "运行需要权限，拒绝可能导致有些功能无法正常运行", 0, *perms)
    }

}

/**
 * 直接申请全部权限
 */
fun Activity.signPermissions() {
    var perms:Array<String> = this.getAllPermissions()
    if (EasyPermissions.hasPermissions(this, *perms)) {

    } else {
        // 有权限被拒绝
        ActivityCompat.requestPermissions(this, perms,0)
    }

}
/**
 * 单个权限申请
 */
fun Activity.signPermission(perms:Array<String>,sign: () -> Unit={}) {
    if (EasyPermissions.hasPermissions(this, *perms)) {
        sign()
    } else {
        ActivityCompat.requestPermissions(this, perms,0);
    }
}


/**
 * 检查权限
 */
fun Activity.checkPermission(perms:Array<String>,go: () -> Unit,fail: () -> Unit): Unit {
    if (EasyPermissions.hasPermissions(this, *perms)) {
        go()
    } else {
        fail()
    }
}







