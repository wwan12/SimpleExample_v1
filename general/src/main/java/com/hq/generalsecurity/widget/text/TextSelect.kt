package com.hq.generalsecurity.widget.text

import android.app.Activity
import android.view.View
import com.google.gson.Gson
import com.hq.generalsecurity.databinding.LayerStandardTextSelectBinding
import com.hq.generalsecurity.expand.Expand
import com.hq.generalsecurity.expand.Expand.getJson
import com.hq.generalsecurity.expand.Option
import com.hq.generalsecurity.expand.TextSet
import com.hq.generalsecurity.widget.form.Parent
import com.hq.tool.toStringPro
import com.hq.tool.widget.openSelect
import com.hq.tool.widget.view.Picker
import com.google.gson.reflect.TypeToken
import java.lang.Exception


class TextSelect: Parent<TextSet, LayerStandardTextSelectBinding>() {
    override fun getViewBindingCls(): Class<LayerStandardTextSelectBinding> {
        return LayerStandardTextSelectBinding::class.java
    }

    override fun init(data:MutableMap<String,Any>?) {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{
            View.INVISIBLE}
        viewBinding.textContent.hint=  line.hint
        if (data!=null&&line.serviceName.isNotEmpty()) {
            line.data = data[line.serviceName].toStringPro()
        }
        viewBinding.textContent.text=  line.data
        viewBinding.textContent.setOnClickListener {
            try {
                //加载外部字典json
                if (line.options!!.size==1&&line.options!![0].id=="Dict"){
                    (viewBinding.textContent.context as Activity).getJson(line.options!![0].title){
                        if (it!=null){
                            val type= object : TypeToken<ArrayList<Option>>() {}.type
                            line.options= Gson().fromJson(it,type)
                        }
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }

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
    override fun getTargetView(): View {
        return viewBinding.textContent
    }
}