package com.hq.tool.system

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.FileProvider
import com.hq.tool.model.webview.HtmlActivity
import java.io.File
import java.io.IOException
import android.text.method.Touch.onTouchEvent
import android.view.MotionEvent
import android.widget.EditText

import androidx.core.content.ContextCompat.getSystemService




/**
 * 复制文本到剪贴板
 *
 * @param text 文本
 */
fun Activity.copyToShear(text: CharSequence) {
//    var clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//    ClipData.newPlainText("text", text).also { clipboard.primaryClip = it }
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
 * 反向输入法
 */
fun Activity.closeInput(view: View) {
    val imm = (this
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
    imm.hideSoftInputFromWindow(view.windowToken,0)
}

/**
 * 使editText点击外部时候失去焦点
 *
 * @param ev 触屏事件
 * @return 事件是否被消费
 */
@Deprecated("重写dispatchTouchEvent")
fun Activity.dispatchTouchEventHelp(ev: MotionEvent): Boolean {
    if (ev.action === MotionEvent.ACTION_DOWN) {
        val v: View? = currentFocus
        if (isShouldHideInput(v, ev)) {
            //点击editText控件外部
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            if (imm != null) {
                assert(v != null)
                //软键盘工具类关闭软键盘
                v?.let { closeInput(it) }
                //使输入框失去焦点
                v?.clearFocus()
            }
        }
        return dispatchTouchEvent(ev)
    }
    // 必不可少，否则所有的组件都不会有TouchEvent了
    return window.superDispatchTouchEvent(ev) || onTouchEvent(ev)
}

/**
 * 判断视图v是否应该隐藏输入软键盘，若v不是输入框，返回false
 *
 * @param v     视图
 * @param event 屏幕事件
 * @return 视图v是否应该隐藏输入软键盘，若v不是输入框，返回false
 */
private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
    if (v != null && v is EditText) {
        val leftTop = intArrayOf(0, 0)
        //获取输入框当前的location位置
        v.getLocationInWindow(leftTop)
        val left = leftTop[0]
        val top = leftTop[1]
        val bottom = top + v.height
        val right = left + v.width
        return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
    }
    return false
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
inline fun <reified T : Activity> Context.startActivity(call:(Intent)->Unit) {
    val intent = Intent(this, T::class.java)
    call(intent)
    startActivity(intent)
}