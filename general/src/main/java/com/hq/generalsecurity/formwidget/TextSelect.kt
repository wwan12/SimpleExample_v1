package com.hq.generalsecurity.formwidget

import android.app.Activity
import android.view.View
import com.hq.generalsecurity.databinding.LayerStandardTextSelectBinding
import com.hq.generalsecurity.expand.TextSet
import com.hq.tool.toStringPro
import com.hq.tool.widget.openSelect
import com.hq.tool.widget.view.Picker

class TextSelect:Parent<TextSet, LayerStandardTextSelectBinding>() {
    override fun getViewBindingCls(): Class<LayerStandardTextSelectBinding> {
        return LayerStandardTextSelectBinding::class.java
    }

    override fun init() {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{
            View.INVISIBLE}
        viewBinding.textContent.hint=  line.hint
        viewBinding.textContent.text=  line.data
        viewBinding.textContent.setOnClickListener {
            val map= arrayListOf<Picker.GetConfigReq>()
            for (o in line.options!!){
                map.add(Picker.GetConfigReq(o.id,o.title))
            }

            viewBinding.textContent.openSelect(viewBinding.root.context as Activity,map){
                if (it!=null){
                    viewBinding.textContent.text=it.msg
                    viewBinding.textContent.tag=it.id
                }
            }
        }
    }

    override fun check(): Boolean {
        return if (line.must){
            viewBinding.textContent.tag!=null
        }else{
            true
        }
    }

    override fun data(): Any? {
        return viewBinding.textContent.tag.toStringPro()
    }
}