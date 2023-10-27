package com.hq.generalsecurity.widget.text

import android.text.InputFilter
import android.view.View
import com.hq.generalsecurity.databinding.LayerStandardTextBinding
import com.hq.generalsecurity.expand.TextSet
import com.hq.generalsecurity.widget.form.Parent
import com.hq.tool.toStringPro


class Text : Parent<TextSet, LayerStandardTextBinding>() {

    override fun getViewBindingCls(): Class<LayerStandardTextBinding> {
        return LayerStandardTextBinding::class.java
    }

    override fun init(data:MutableMap<String,Any>?) {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        viewBinding.textContent.hint=  line.hint
        if (data!=null&&line.serviceName.isNotEmpty()) {
            line.data = data[line.serviceName].toStringPro()
        }
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

    override fun getTargetView(): View {
        return viewBinding.textContent
    }

}