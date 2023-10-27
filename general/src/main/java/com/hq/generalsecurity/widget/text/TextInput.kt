package com.hq.generalsecurity.widget.text

import android.text.InputFilter
import android.text.InputType
import android.view.View
import com.hq.generalsecurity.databinding.LayerStandardTextInputBinding
import com.hq.generalsecurity.expand.Rule
import com.hq.generalsecurity.expand.TextSet
import com.hq.generalsecurity.widget.form.Parent
import com.hq.tool.loge
import com.hq.tool.toStringPro

class TextInput: Parent<TextSet, LayerStandardTextInputBinding>() {
    override fun getViewBindingCls(): Class<LayerStandardTextInputBinding> {
        return LayerStandardTextInputBinding::class.java
    }

    override fun init(data:MutableMap<String,Any>?) {
        "input:${line.title}--${line.type}".loge()

        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{
            View.INVISIBLE}
        viewBinding.textContent.hint=  line.hint
        if (data!=null&&line.serviceName.isNotEmpty()) {
            line.data = data[line.serviceName].toStringPro()
        }
        viewBinding.textContent.setText(line.data)
        if (line.rule!=null){
            when(line.rule){
                Rule.Number->viewBinding.textContent.inputType= InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                Rule.Phone->{
                    viewBinding.textContent.inputType= InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                }
            }
        }
        if (line.maxLength>0){
            viewBinding.textContent.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(line.maxLength))
        }
    }

    override fun data(): Any? {
        return viewBinding.textContent.text.toString()
    }

    override fun check(): Boolean {
        return if (line.must){
            viewBinding.textContent.text.isNotEmpty()
        }else{
            true
        }
    }

    override fun getTargetView(): View {
        return viewBinding.textContent
    }
}