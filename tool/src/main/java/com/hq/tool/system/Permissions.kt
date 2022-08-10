package com.hq.tool.system

import android.app.Activity
import androidx.core.app.ActivityCompat
//import com.hjq.permissions.OnPermissionCallback
//import com.hjq.permissions.Permission
//import com.hjq.permissions.XXPermissions
import com.hq.tool.toast

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

public enum class PermissionState{
    Pass,
    Refuse,
    PermanentRefuse,
    Unknown
}

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
fun Activity.signPermission(vararg perms:String,sign: () -> Unit={}) {
    if (EasyPermissions.hasPermissions(this, *perms)) {
        sign()
    } else {
        ActivityCompat.requestPermissions(this, perms,0);
    }
}


/**
 * 检查权限
 */
fun Activity.checkPermission(vararg perms:String): Boolean {
   return EasyPermissions.hasPermissions(this, *perms)
}

//fun Activity.signPermissionNew(sign: (PermissionState,permissions: MutableList<String>) -> Unit): Unit {
//    var perms:Array<String> = this.getAllPermissions()
//    var signPermission:ArrayList<String> = arrayListOf()
//    for (p in perms){
//        if (XXPermissions.isSpecial(p)){
//            signPermission.add(p)
//        }
//
//    }
//    XXPermissions.with(this)
//        // 申请单个权限
////        .permission(Permission.RECORD_AUDIO)
//        // 申请多个权限
//        .permission(signPermission)
//        // 设置权限请求拦截器（局部设置）
//        //.interceptor(new PermissionInterceptor())
//        // 设置不触发错误检测机制（局部设置）
//        //.unchecked()
//        .request(object : OnPermissionCallback {
//
//            override fun onGranted(permissions: MutableList<String>, all: Boolean) {
//                if (all) {
//                    sign(PermissionState.Pass,permissions)
////                    toast("获取录音和日历权限成功")
//                } else {
//                    sign(PermissionState.Refuse,permissions)
////                    toast("获取部分权限成功，但部分权限未正常授予")
//                }
//            }
//
//            override fun onDenied(permissions: MutableList<String>, never: Boolean) {
//                if (never) {
//                    sign(PermissionState.PermanentRefuse,permissions)
////                    "被永久拒绝授权，请手动授予权限".toast(this@signPermissionNew)
//                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
////                    XXPermissions.startPermissionActivity(this@signPermissionNew, permissions)
//                } else {
//                    sign(PermissionState.Refuse,permissions)
////                    "获取录音和日历权限失败".toast(this@signPermissionNew)
//                }
//            }
//        })
//}
//
//
///**
// * 单个权限申请
// */
//fun Activity.signPermissionNew(vararg perms:String,sign: (PermissionState,permissions: MutableList<String>) -> Unit={a,b->}) {
//    XXPermissions.with(this)
//        // 申请单个权限
////        .permission(Permission.RECORD_AUDIO)
//        // 申请多个权限
//        .permission(perms)
//        // 设置权限请求拦截器（局部设置）
//        //.interceptor(new PermissionInterceptor())
//        // 设置不触发错误检测机制（局部设置）
//        //.unchecked()
//        .request(object : OnPermissionCallback {
//
//            override fun onGranted(permissions: MutableList<String>, all: Boolean) {
//                if (all) {
////                    toast("获取录音和日历权限成功")
//                    sign(PermissionState.Pass,permissions)
//                } else {
////                    toast("获取部分权限成功，但部分权限未正常授予")
//                    sign(PermissionState.Refuse,permissions)
//                }
//            }
//
//            override fun onDenied(permissions: MutableList<String>, never: Boolean) {
//                if (never) {
////                    "被永久拒绝授权，请手动授予权限".toast(this@signPermissionNew)
//                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
//                    sign(PermissionState.PermanentRefuse,permissions)
//                } else {
//                    sign(PermissionState.Refuse,permissions)
////                    "获取录音和日历权限失败".toast(this@signPermissionNew)
//                }
//            }
//        })
//}
//
//
//fun Activity.goSystemPermission(vararg perms:String): Unit {
//    XXPermissions.startPermissionActivity(this@goSystemPermission, perms)
//
//}
//
///**
// * 检查权限
// */
//fun Activity.checkPermissionNew(vararg perms:String): Boolean {
//    return XXPermissions.isGranted(this,perms)
//}
//
