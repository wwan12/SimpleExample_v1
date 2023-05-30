package com.hq.generalsecurity.formwidget

import android.view.View
import com.hq.generalsecurity.databinding.LayerStandardTextCheckBinding
import com.hq.generalsecurity.expand.TextSet
import com.hq.tool.toBooleanPro
import com.hq.tool.toStringPro

class TextCheck:Parent<TextSet, LayerStandardTextCheckBinding>() {
    override fun getViewBindingCls(): Class<LayerStandardTextCheckBinding> {
        return LayerStandardTextCheckBinding::class.java
    }

    override fun init() {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{
            View.INVISIBLE}

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

}