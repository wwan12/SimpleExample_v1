package com.hq.general.widget.text

import android.app.Activity
import android.view.View
import com.google.gson.Gson
import com.hq.general.databinding.LayerStandardTextSelectBinding
import com.hq.general.expand.Expand.getJson
import com.hq.general.model.Option
import com.hq.general.model.TextSet
import com.hq.general.widget.form.Parent
import com.hq.tool.toStringPro
import com.hq.tool.widget.openSelect
import com.hq.tool.widget.view.Picker
import com.google.gson.reflect.TypeToken
import com.hq.general.expand.StorageExpand
import com.hq.general.extraction.DataExtraction
import java.lang.Exception


class TextSelect: Parent<TextSet, LayerStandardTextSelectBinding>() {
    override fun getViewBindingCls(): Class<LayerStandardTextSelectBinding> {
        return LayerStandardTextSelectBinding::class.java
    }

    override fun init() {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{
            View.INVISIBLE}
        viewBinding.textContent.hint=  line.hint
        if (line.serviceName.isNotEmpty()) {
            line.data = data.data.toStringPro()
        }

        viewBinding.textContent.setOnClickListener {
            try {
                //加载外部字典json
                    if (line.options==null){
                        line.options= StorageExpand.getLocalData<ArrayList<Option>>(line.serviceName)
                    }else{
                        if (line.options!!.size==1&&line.options!![0].id=="Dict"){
                            (viewBinding.textContent.context as Activity).getJson(line.options!![0].title){
                                if (it!=null){
                                    val type= object : TypeToken<ArrayList<Option>>() {}.type
                                    line.options= Gson().fromJson(it,type)
                                }
                            }
                        }
                    }
            }catch (e:Exception){
                e.printStackTrace()
            }

            val map= arrayListOf<Picker.GetConfigReq>()
            if (line.options!=null){
                for (o in line.options!!){
                    map.add(Picker.GetConfigReq(o.id,o.title))
                    if (line.data==o.id){
                        viewBinding.textContent.text=  o.title
                    }
                }
            }


            viewBinding.textContent.openSelect(viewBinding.root.context as Activity,map){
                if (it!=null){
                   // select(line.options!![map.indexOf(it)])
                    viewBinding.textContent.text=it.msg
                    viewBinding.textContent.tag=it.id

                }
            }
        }
    }

    fun select(option: Option): Unit {
        viewBinding.textContent.text=option.title
        viewBinding.textContent.tag=option.id
        if (option.childOptions==null){

        }else{

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
    override fun getTargetView(): View {
        return viewBinding.textContent
    }
    override fun refresh() {

    }
}