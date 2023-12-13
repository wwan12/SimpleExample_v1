package com.hq.general.widget.group

import android.app.Activity
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hq.general.databinding.LayerSelectGroupBinding
import com.hq.general.databinding.LayerStandardGroupBinding
import com.hq.general.expand.Expand
import com.hq.general.expand.Expand.getJson
import com.hq.general.expand.Global
import com.hq.general.expand.StorageExpand
import com.hq.general.extraction.DataExtraction
import com.hq.general.model.CacheType
import com.hq.general.model.DataType
import com.hq.general.model.GroupSet
import com.hq.general.model.Option
import com.hq.general.widget.form.Parent
import com.hq.tool.loge
import com.hq.tool.toStringPro
import com.hq.tool.widget.openSelect
import com.hq.tool.widget.view.Picker
import java.lang.Exception


class SelectGroup : Parent<GroupSet, LayerSelectGroupBinding>() {

    var options:ArrayList<Option>?=null

    val childs= arrayListOf<Parent<*,*>>()

    override fun getViewBindingCls(): Class<LayerSelectGroupBinding> {
        return LayerSelectGroupBinding::class.java
    }

    override fun init() {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        viewBinding.textContent.hint="请选择${line.title}"
        for (child in line.childs) {
            val c = child.place(viewBinding.textTitle.context as Activity, Global.getDataExtraction("",CacheType.None,child.serviceName,child.dataType))
            if (child.dataType!=DataType.OnlyData){
                viewBinding.listLl.addView(c.viewBinding.root)
            }
            childs.add(c)
        }

        if (line.serviceName.isNotEmpty()) {
            line.data = data.data.toStringPro()
        }

        options= StorageExpand.getLocalData<ArrayList<Option>>(line.serviceName)

        val map= arrayListOf<Picker.GetConfigReq>()
        options.toStringPro().loge("SelectGroup")
        line.data.toStringPro().loge("SelectGroup")
        if (options!=null){
            for (o in options!!){
                map.add(Picker.GetConfigReq(o.id,o.title))
                if (line.data==o.id){
                    setChild(line.data)
                }
            }
        }

        viewBinding.textContent.setOnClickListener {
            viewBinding.textContent.openSelect(viewBinding.root.context as Activity,map){
                if (it!=null){
                    // select(line.options!![map.indexOf(it)])
                    line.data=it.id
                    setChild(it.id)
                }
            }
        }
    }

    fun setChild(id:String): Unit {
        try {
            if (options != null) {
                for (o in options!!){
                    if (id==o.id){
                        viewBinding.textContent.text=  o.title
                        for (child in childs){
                            val data= o.childOptions?.find { it.id==child.line.serviceName }
                            child.data.data=data?.title
                            child.refresh()
                        }
                    }
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun data(): Any {
        val map= mutableMapOf<String,Any>()
        map[line.serviceName] = line.data
        for (child in childs){
            map[child.line.serviceName] = child.data.data.toStringPro()
        }
       return  map
    }

    override fun check(): Boolean {
        return line.data.isNotEmpty()
    }

    override fun getTargetView(): View {
        return viewBinding.root
    }

    override fun refresh() {

    }

}