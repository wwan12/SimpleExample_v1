package com.hq.tool.widget.view

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup

/**
 * 文件描述：
 * 作者：Administrator
 * 创建时间：2018/9/14/014
 * 更改时间：2018/9/14/014
 * 版本号：1
 *
 */
class BottomRadioView(context: Context?) : LinearLayout(context) {

    val bottomRBs = ArrayList<RadioButton>()
    var bottomRg: RadioGroup? = null
    var chengRB: RadioButton? = null
    var noCheckDrawableId = 0
    var checkDrawableId = 0
    var noCheckColorId = 0
    var checkColorId = 0
    var drawTopIds:List<Int>?=null
    var chengDrawTopIds:List<Int>?=null

    fun setChengDrawable(nocheckid: Int, checkid: Int): Unit {
        for (rb in bottomRBs) {
            rb.setButtonDrawable(nocheckid)
        }
        this.noCheckDrawableId = nocheckid
        this.checkDrawableId = checkid
    }

    fun setChengColor(nocheck: Int, checked: Int): Unit {
        for (rb in bottomRBs) {
            rb.setBackgroundColor(nocheck)
        }
        this.noCheckColorId = nocheck
        this.checkColorId = checked
    }

    fun setTextColor(colorId: Int): Unit {
        for (rb in bottomRBs) {
            rb.setTextColor(colorId)
        }
    }

    fun setTextSize(size: Float): Unit {
        for (rb in bottomRBs) {
            rb.setTextSize(size)
        }
    }

    private fun chengBackground(rb: RadioButton,i: Int): Unit {
        if (noCheckDrawableId != 0) {
            rb.setBackgroundResource(checkDrawableId)
            chengRB?.setBackgroundResource(noCheckDrawableId)
        }
        if (noCheckColorId != 0) {
            rb.setBackgroundColor(checkColorId)
            chengRB?.setBackgroundColor(noCheckColorId)
        }
        if (chengDrawTopIds!=null){
            rb.setCompoundDrawables(rb.compoundDrawables[0], chengDrawTopIds?.get(i)?.let { resources.getDrawable(it) },rb.compoundDrawables[2],rb.compoundDrawables[3] )
            chengRB?.setCompoundDrawables(chengRB?.compoundDrawables?.get(0), drawTopIds?.get(i)?.let { resources.getDrawable(it) }, chengRB?.compoundDrawables?.get(2), chengRB?.compoundDrawables?.get(3))
        }
        chengRB = rb
    }

    fun setChengEvent(event: (Int) -> Unit): Unit {
        var i = 0
        for (rb in bottomRBs) {
            rb.setOnClickListener {
                event(i)
                chengBackground(it as RadioButton,i)
                i++
            }
        }
    }


    fun setBottomString(list: List<String>, colorId: Int = 0, size: Float = 14f) {
        val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        bottomRg = RadioGroup(context)
        bottomRg?.orientation = LinearLayout.HORIZONTAL
        bottomRg?.layoutParams=param
        for (l in list) {
            val newRB = RadioButton(context)
            newRB.setText(l)
            if (colorId != 0) {
                newRB.setTextColor(colorId)
            }
            newRB.setTextSize(size)
            newRB.gravity = Gravity.CENTER
            newRB.layoutParams = param
            newRB.setButtonDrawable(null)
            bottomRBs.add(newRB)
            bottomRg?.addView(newRB)
        }
    }

    fun setDrawableTop(drawTopIds: List<Int>,chengDrawTopIds: List<Int>? = null): Unit {
        var i = 0
        for (rb in bottomRBs){
            rb.setCompoundDrawables(rb.compoundDrawables[0],resources.getDrawable(drawTopIds[i]),rb.compoundDrawables[2],rb.compoundDrawables[3] )
            i++
        }
        this.drawTopIds=drawTopIds
        this.chengDrawTopIds=chengDrawTopIds
    }


}