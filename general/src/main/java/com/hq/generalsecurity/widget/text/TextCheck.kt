package com.hq.generalsecurity.widget.text

import android.view.View
import com.hq.generalsecurity.databinding.LayerStandardTextCheckBinding
import com.hq.generalsecurity.expand.TextSet
import com.hq.generalsecurity.widget.form.Parent
import com.hq.tool.toStringPro

class TextCheck: Parent<TextSet, LayerStandardTextCheckBinding>() {
    override fun getViewBindingCls(): Class<LayerStandardTextCheckBinding> {
        return LayerStandardTextCheckBinding::class.java
    }

    override fun init(data:MutableMap<String,Any>?) {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{
            View.INVISIBLE}

        if (data!=null&&line.serviceName.isNotEmpty()) {
            line.data = data[line.serviceName].toStringPro()
        }
        viewBinding.textContent.text=" "
        for (o in line.options!!){
            if (o.id==line.data){
                viewBinding.textContent.isChecked=o.title.toBooleanStrict()
            }
        }

        viewBinding.textContent.setOnCheckedChangeListener { buttonView, isChecked ->
            for (o in line.options!!){
                if (o.title==isChecked.toString()){
                    buttonView.tag=o.id
                }
            }
        }
        if ( viewBinding.textContent.tag==null|| viewBinding.textContent.tag==""){
            viewBinding.textContent.tag= line.options!!.find {it.title==false.toString()  }!!.id
        }
    }

    override fun check(): Boolean {
        return true
    }

    override fun data(): Any? {
        return  viewBinding.textContent.tag.toStringPro()
    }

    override fun getTargetView(): View {
        return  viewBinding.textContent
    }

}