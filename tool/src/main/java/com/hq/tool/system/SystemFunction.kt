package com.hq.tool.system

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.hq.tool.model.webview.HtmlActivity
import java.io.File

/**
 * 复制文本到剪贴板
 *
 * @param text 文本
 */
@Deprecated("安卓10不可用")
fun Activity.copyToShear(text: CharSequence) {
    var clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    ClipData.newPlainText("text", text).also { clipboard.primaryClip = it }
}



/**
 * 获取剪贴板的文本
 *
 * @return 剪贴板的文本
 */
fun Activity.getShearText(): CharSequence? {
    val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = clipboard.primaryClip
    return if (clip != null && clip.itemCount > 0) {
        clip.getItemAt(0).coerceToText(this)
    } else null
}

@SuppressLint("MissingPermission")
fun Activity.openCall(phone: String) {
    val uri = Uri.parse("tel:" + phone.trim())
    this.startActivity(Intent(Intent.ACTION_CALL, uri))
}

//打开APK程序代码

fun Activity.installApk(file: File) {
   val intent = Intent(Intent.ACTION_VIEW)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        val uri = FileProvider.getUriForFile(this,  this.packageName+".fileProvider", file)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
    } else {
        try {
            val command = arrayOf(this.toString())
            val builder = ProcessBuilder(*command)
            builder.start()
        } catch (ignored: IOException) {
        }
        val uri = Uri.fromFile(file)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
    }
    this.startActivity(intent)
}

/**
 * 隐藏输入法
 */
@Deprecated("页面启动无效")
fun Activity.hideInput() {
    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}

/**
 * 反向输入法
 */
fun Activity.closeInput() {
    val imm = (this
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
}

/**
 * 打开应用内网页
 * isAutoStyle自动适配（性能会受到较大影响）
 */
fun Activity.openHtml(url:String,errorUrl:String="",isSingle:Boolean=false,isAutoStyle:Boolean=false): Unit {
    val ins=Intent(this,HtmlActivity::class.java)
    ins.putExtra("URL",url)
    ins.putExtra("ERRORURL",errorUrl)
    ins.putExtra("ISSINGLE",isSingle)
    ins.putExtra("ISAUTOSTYLE",isAutoStyle)
    this.startActivity(ins)
}
inline fun <reified T : Activity> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}