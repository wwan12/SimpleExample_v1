package com.aisino.tool.system

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

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