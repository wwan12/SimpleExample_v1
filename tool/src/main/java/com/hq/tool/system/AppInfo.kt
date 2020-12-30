package com.hq.tool.system

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.WindowManager
import android.content.pm.PackageManager.NameNotFoundException

/**
 * 获取系统数据
 * Created by lenovo on 2018/2/26.
 */

/**
 * 获取当前程序的版本号
 */
fun Activity.getVersionCode(): Int {
    val manager = this.application.getPackageManager()
    try {
        val info = manager.getPackageInfo(
                this.application.getPackageName(), 0)
        val versionCode = info.versionCode
//        Log.i(TAG, "versionCode = " + versionCode)
        return versionCode
    } catch (e: PackageManager.NameNotFoundException) {
    }

    return 1
}

/**
 * 获取当前程序的版本名称
 */
fun Activity.getVersionName(): String {
    val manager = this.application.getPackageManager()
    try {
        val info = manager.getPackageInfo(
                this.application.getPackageName(), 0)
        return info.versionName
    } catch (e: NameNotFoundException) {
        
    }
    return "1.0"
}


/**
 * 获取程序的名字
 * @param context
 * @param packname
 * @return
 */
fun  Activity.getAppName():String?{
    //包管理操作管理类
    val pm = this.getPackageManager()
    try {
        val info = pm.getApplicationInfo(this.packageName, 0)
        return info.loadLabel(pm).toString();
        } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace();
        }
    return null
}

/*
* 获取程序的权限
*/
fun Activity.getAllPermissions(): Array<String> {
    try {
        //包管理操作管理类
        val pm = this.packageManager
        val packinfo = pm.getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
        //获取到所有的权限
        return packinfo.requestedPermissions
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return emptyArray()
}

/**
 * 获取程序的签名
 * @return
 */
fun Activity.getAppSignature(): String {
    try {
        //包管理操作管理类
        val pm = this.packageManager
        val packinfo = pm.getPackageInfo(this.packageName, PackageManager.GET_SIGNATURES)
        //获取当前应用签名
        return packinfo.signatures[0].toCharsString()
    } catch (e: NameNotFoundException) {
        e.printStackTrace()
    }

    return ""
}

/**
 * 获取程序的包名
 * @return
 */
fun Activity.getAppId(): String {
    try {
        //包管理操作管理类
        return application.packageName
    } catch (e: NameNotFoundException) {
        e.printStackTrace()
    }

    return ""
}


/**
 * 获取手机IMEI
 */
@SuppressLint("MissingPermission")
@Deprecated("部分手机有权限也拿不到")
fun Activity.getIMEICode(): String {
    val telephonyManager = this.application
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    var IMEI = telephonyManager.deviceId
    if (IMEI == null) {
        IMEI = Settings.System.getString(this.application.getContentResolver(),
                Settings.System.ANDROID_ID)
    }
    return IMEI
}

/**
 * @param slotId  slotId为卡槽Id，它的值为 0、1；
 * @return
 */
@Deprecated("部分手机有权限也拿不到")
fun Activity.getIMEI( slotId: Int): String {
    try {
        val manager = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val method = manager.javaClass.getMethod("getImei", Int::class.javaPrimitiveType)
        return method.invoke(manager, slotId) as String
    } catch (e: Exception) {
        return ""
    }
}

fun Activity.getOperator(): String {
    val telephonyManager = this.application
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    var operator = telephonyManager.simOperatorName
    if (operator == null) {
        operator = telephonyManager.networkOperatorName
    }
    return operator
}

/**
 * 打开手机 网络设置界面
 */
fun Activity.openNetworkSettings() {
    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
    this.startActivity(intent)
}


/**
 * 判断网络连接
 */
@SuppressLint("MissingPermission")
fun Activity.isNetworkAvailable(): Boolean {
    val connectivity = this.application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = connectivity?.allNetworkInfo
    if (info != null) {
        for (i in info.indices) {
            if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
    }
    return false
}

/**
 * 获取SDK版本
 */
fun Activity.getSdkVersion(): Int {
    return Build.VERSION.SDK_INT
}

/**
 * 获取手机屏幕宽度
 */
fun Activity.getScreenWidth(): Int {
    val display = (this.application
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    return display.width
}

/**
 * 获取手机屏幕高度
 */
fun Activity.getScreenHeight(): Int {
    val display = (this.application
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    return display.height
}

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun Activity.dip2px(dpValue: Float): Int {
    val scale = this.getResources().getDisplayMetrics().density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun Activity.px2dip(pxValue: Float): Int {
    val scale = this.getResources().getDisplayMetrics().density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * 判断手机定位
 */
fun Activity.isLocationEnabled(): Boolean {
    val manager = this.application
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (manager != null) {
        val gps = manager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)
        val net = manager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || net
    }
    return false
}
