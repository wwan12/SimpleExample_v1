package com.hq.generalsecurity.formwidget

import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.hq.generalsecurity.R
import com.hq.generalsecurity.databinding.LayerStandardTextBinding
import com.hq.generalsecurity.expand.Rule
import com.hq.generalsecurity.expand.TextSet


class Text :Parent<TextSet,LayerStandardTextBinding>() {

    override fun getViewBindingCls(): Class<LayerStandardTextBinding> {
        return LayerStandardTextBinding::class.java
    }

    override fun init() {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        viewBinding.textContent.hint=  line.hint
        viewBinding.textContent.text=  line.data
        if (line.maxLength>0){
            viewBinding.textContent.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(line.maxLength))
        }
    }

    override fun data(): Any {
       return viewBinding.textContent.text.toString()
    }

    override fun check(): Boolean {
        return true
    }

}