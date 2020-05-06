package com.aisino.tool.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.regex.Pattern

/**
 * 文件描述：
 * 作者：Administrator
 * 创建时间：2018/12/10/010
 * 更改时间：2020/5/6/010
 * 版本号：1
 *
 */

private val nums = arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")

private val pos_units = arrayOf("", "十", "百", "千")

private val weight_units = arrayOf("", "万", "亿")

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



/**
 * 数字转汉字
 */
fun Int.numberToChinese(): String {
    var num=this
    if (num == 0) {
        return "零"
    }
    var weigth = 0//节权位
    var chinese = ""
    var chinese_section = ""
    var setZero = false//下一小节是否需要零，第一次没有上一小节所以为false
    while (num > 0) {
        val section = num % 10000//得到最后面的小节
        if (setZero) {//判断上一小节的千位是否为零，是就设置零
            chinese = nums[0] + chinese
        }
        chinese_section = sectionTrans(section)
        if (section != 0) {//判断是都加节权位
            chinese_section = chinese_section + weight_units[weigth]
        }
        chinese = chinese_section + chinese
        setZero = section < 1000 && section > 0
        num = num / 10000
        weigth++
    }
    if ((chinese.length == 2 || chinese.length == 3) && chinese.contains("一十")) {
        chinese = chinese.substring(1, chinese.length)
    }
    if (chinese.indexOf("一十") == 0) {
        chinese = chinese.replaceFirst("一十".toRegex(), "十")
    }

    return chinese
}

private fun sectionTrans(section: Int): String {
    var section = section
    val section_chinese = StringBuilder()
    var pos = 0//小节内部权位的计数器
    var zero = true//小节内部的置零判断，每一个小节只能有一个零。
    while (section > 0) {
        val v = section % 10//得到最后一个数
        if (v == 0) {
            if (!zero) {
                zero = true//需要补零的操作，确保对连续多个零只是输出一个
                section_chinese.insert(0, nums[0])
            }
        } else {
            zero = false//有非零数字就把置零打开
            section_chinese.insert(0, pos_units[pos])
            section_chinese.insert(0, nums[v])
        }
        pos++
        section = section / 10
    }

    return section_chinese.toString()
}

/**
 * 汉字转数字
 */
fun String.getNumFromString(): Int {
    var index=0
    if (isContainsNumber(this)) {
        index = getStringNum(this).toInt()
    }else{
        index = getCharNum(this)
    }

    return index
}
///最大9999
private fun getCharNum(number: String): Int {
    var cStr=""
    var sStr=""
    for (unit in pos_units){
        cStr= number.replace(unit,"")
    }
    for (s in cStr){
        if (nums.contains(s.toString())){
            sStr+= nums.indexOf(s.toString()).toString()
        }else{
            return 0
        }
    }
    val ri=sStr.toIntOrNull()
    if (ri==null){
        return 0
    }else{
        return ri
    }
}

private fun isContainsNumber(time: String): Boolean {
    var isNum = false
    val regEx = "[^0-9]"
    val p = Pattern.compile(regEx)
    val m = p.matcher(time)
    val trim = m.replaceAll("").trim()
    return trim.length>0
}

private fun getStringNum(time: String): String {
    val regEx = "[^0-9]"
    val p = Pattern.compile(regEx)
    val m = p.matcher(time)
    var trim = m.replaceAll("").trim()
    if (trim.length == 1) {
        trim = "0$trim"
    }
    return trim
}

