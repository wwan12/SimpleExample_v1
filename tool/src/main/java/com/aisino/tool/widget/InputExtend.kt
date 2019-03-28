package com.aisino.tool.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * 文件描述：
 * 作者：Administrator
 * 创建时间：2018/12/10/010
 * 更改时间：2018/12/10/010
 * 版本号：1
 *
 */

/**
 * 设置为会记计数 1，000，000
 */
fun EditText.accounting(): Unit {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.toString().length > 3 && start != 0 && count != s.length) {
                val sb = StringBuffer(s.toString().replace(",".toRegex(), ""))
                val index = (sb.toString().length - 1) / 3
                for (i in 0 until index) {
                    sb.insert(sb.toString().length - 3 * (i + 1) - i, ",")
                }
                this@accounting.setText(sb.toString())
                this@accounting.setSelection(this@accounting.getText().length)
            }
        }

        override fun afterTextChanged(s: Editable) {}
    })
}