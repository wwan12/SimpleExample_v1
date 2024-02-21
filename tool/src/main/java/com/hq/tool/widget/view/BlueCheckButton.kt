package com.hq.tool.widget.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatRadioButton
import com.hq.tool.R
import com.hq.tool.loge
import kotlin.jvm.JvmOverloads

class BlueCheckButton constructor(context: Context, attrs: AttributeSet? = null) : androidx.appcompat.widget.AppCompatCheckBox(context, attrs) {

    var listener: OnCheckedChangeListener?=null

    init {
        init(context, attrs)
    }
    private fun init(context: Context, attrs: AttributeSet?) {
        super. setOnCheckedChangeListener { buttonView, isChecked ->
            isChecked.toString().loge("aaaaaa")
             if (isChecked){
                 background =  resources.getDrawable(R.drawable.check_blue_button)
                setTextColor(resources.getColor(R.color.bluePrimary) )
                 buttonView.setTypeface(null,Typeface.BOLD)
            }else{
                 background = resources.getDrawable(R.drawable.check_gary_button)
                 setTextColor(resources.getColor(R.color.blackPrimary) )
                 buttonView.setTypeface(null,Typeface.NORMAL)
            }
            listener?.onCheckedChanged(buttonView,isChecked)
        }


        background = resources.getDrawable(R.drawable.check_gary_button)
        buttonDrawable=null
        setPadding(36,16,36,16)
    }

    override fun setOnCheckedChangeListener(listener: OnCheckedChangeListener?) {
//        super.setOnCheckedChangeListener(listener)
        this.listener=listener
    }
}