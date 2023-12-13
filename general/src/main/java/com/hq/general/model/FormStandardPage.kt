package com.hq.general.model

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.hq.general.control.ControlBase
import com.hq.general.databinding.LayerStandardLineBinding
import com.hq.general.databinding.StandardFormBinding
import com.hq.general.expand.Expand
import com.hq.general.expand.Global
import com.hq.general.widget.form.OnlyData
import com.hq.general.widget.form.Parent
import com.hq.general.widget.group.SelectGroup
import com.hq.tool.loge
import com.hq.tool.misc.Reflect
import com.hq.tool.onLoad
import com.hq.tool.toStringPro
import com.hq.tool.toast
import java.util.ArrayList

data class FormStandardPage(var load: UrlInfo?, var post: UrlInfo?, var lineSets: ArrayList<LineSet>): PageSet(
    VersionInfo("1",""),
    null,
    PageType.FormStandard,
    "",
    PageAction()
){
    fun getView(activity: Activity, control: ControlBase<*>?, call:(StandardFormBinding, MutableList<Parent<*, *>>)->Unit): Unit {
        val lines = mutableListOf<Parent<*, *>>()
        val binding = StandardFormBinding.inflate(activity.layoutInflater)

//按钮1
        val actionText= action.pageBottom?.name//actionText(activity,binding.standardActionButton)
        if (actionText==null){
            binding.standardActionButton.visibility=View.GONE
        }else{
            binding.standardActionButton.text=actionText
        }
//按钮2
        val actionText2= action.pageBottom2?.name//actionText(activity,binding.standardActionButton)
        if (actionText2==null){
            binding.standardActionButton2.visibility=View.GONE
        }else{
            binding.standardActionButton2.text=actionText2
        }
        load?.load({
            if (it.tryGet<String>("code") == "200") {
                for (line in lineSets) {
                    var view: View? = null
                    val de= Global.getDataExtraction(it.url,line.source ?: CacheType.None,line.serviceName,line.dataType)
                    de.fromData(it)
                    val ts = line.place(activity,de)
                    lines.add(ts)
                    when (line.dataType) {
                        DataType.OnlyData -> {

                        }
                        else -> {
                            view = ts.viewBinding.root
                        }
                    }

                    if (view != null) {
                        if (line.serviceName.isNotEmpty()) {
                            view.tag =
                                "${line.dataType}:${line.servicePath}:${line.serviceName}"
                        }
                        val lineBinding = LayerStandardLineBinding.inflate(activity.layoutInflater)

                        binding.standardLl.addView(view)
                        binding.standardLl.addView(lineBinding.root)
                    }
                    control?.onLayoutCreate(ts)
                }
            }else{
                for (line in lineSets) {
                    val de= Global.getDataExtraction("",line.source ?: CacheType.None,line.serviceName,line.dataType)
                    de.fromData(null)
                    val ts = line.place(activity,de)
                    lines.add(ts)
                    when (line.dataType) {
                        DataType.OnlyData -> {

                        }
                        else -> {
                            val lineBinding = LayerStandardLineBinding.inflate(activity.layoutInflater)

                            binding.standardLl.addView(ts.viewBinding.root)
                            binding.standardLl.addView(lineBinding.root)
                        }
                    }
                    control?.onLayoutCreate(ts)
                }
            }
            call(binding,lines)

        },{
            it.failMsg.toast(activity)
        })
    }
    

    fun check(activity: Activity,lines:MutableList<Parent<*, *>>,go:()->Unit) {
        for (r in lines){
            if (!r.check()){
                when(r.line.dataType){
                    DataType.Text ->{
                        ( r.line as TextSet).hint.toast(activity)
                    }
                    DataType.Group ->{
                        if (( r.line as GroupSet).type==GroupType.SelectGroup){
                           "请选择${r.line.title}".toast(activity)
                        }

                    }
                    DataType.Img,DataType.File ->{
                        "请上传${r.line.title }".toast(activity)
                    }
                }
                "${r.line.title}未通过".loge()
                return
            }
        }
        go()
    }

    fun collectData(lines:MutableList<Parent<*, *>>): Unit {
        for (r in lines){
            val rdata = r.data()
            if (rdata!=null){
                if (post?.loadUrl?.isNotEmpty()==true) {
                    if (rdata is MutableMap<*,*>){
                        for (dr in rdata as MutableMap<String,Any>) {
                            setLoadParam(post!!.loadParams,dr.key,dr.value)
                        }
                    }else{
                        setLoadParam(post!!.loadParams,r.line.serviceName, rdata)
                    }
                }
            }
        }
    }

    fun upload(activity: Activity,call:(Boolean)->Unit): Unit {
        val load=activity.onLoad()
        Expand.http(post, {
            if (it.get<String>("code") == "200") {
                "${action.pageBottom?.name}成功".toast(activity)
                call(true)
            } else {
                it.get<String>("msg").toast(activity)
                call(false)
            }
            load?.dismiss()
        }, {
            load?.dismiss()
            it.failMsg.toast(activity)
        })
    }

    fun setLoadParam(params:ArrayList<LoadParam>,key:String,value:Any): Unit {
        val param = params.find { it.apiName == key }
        if (param == null) {
            post?.loadParams?.add(
                LoadParam(
                    key, value, key,
                    CacheType.None
                )
            )
        } else {
            param.def = value
        }
    }
}