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
fun Activity.copyToShear(text: CharSequence) {
    val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.primaryClip = ClipData.newPlainText("text", text)
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
@Deprecated("安卓10不可用")
fun Activity.installApk(file: File) {
    Log.e("OpenFile", file.name)
    val intent = Intent()
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.action = android.content.Intent.ACTION_VIEW
    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
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