package com.hq.generalsecurity.expand

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.hq.generalsecurity.BaseActivity
import com.hq.generalsecurity.databinding.ActivityStandardFormBinding
import com.hq.generalsecurity.databinding.StandardFormBinding
import com.hq.generalsecurity.widget.form.OnlyData
import com.hq.generalsecurity.widget.form.Parent
import com.hq.tool.onLoad
import com.hq.tool.toStringPro
import com.hq.tool.toast
import java.util.ArrayList

data class FormStandardPage(var load: UrlInfo?,var post: UrlInfo?,var lineSets: ArrayList<LineSet>): PageSet(
    VersionInfo("1",""),
    PageType.FormStandard,
    "",
    PageAction(FormAction.None,FormAction.None)
){
    fun getView(activity: Activity,call:(StandardFormBinding,MutableList<Parent<*, *>>)->Unit): Unit {
        val lines = mutableListOf<Parent<*, *>>()
        val binding = StandardFormBinding.inflate(activity.layoutInflater)

        val actionText= actionText()
        if (actionText.isEmpty()){
            binding.standardActionButton.visibility= View.GONE
        }else{
            binding.standardActionButton.text=actionText
        }

        if (load?.loadUrl?.isNotEmpty() == true) {
            Expand.http(load, {
                if (it.get<String>("code") == "200") {
                    for (line in lineSets) {
                        var view: View? = null
                        when (line.dataType) {
                            DataType.OnlyData -> {
                                val od = OnlyData()
                                od.line = line
                                lines.add(od)
                            }
                            else -> {
                                val ts = line.place(activity,it.data)
                                lines.add(ts)
                                view = ts.viewBinding.root
                            }
                        }

                        if (view != null) {
                            if (line.serviceName.isNotEmpty()) {
                                view.tag =
                                    "${line.dataType}:${line.servicePath}:${line.serviceName}"
                            }
                            binding.standardLl.addView(view)
                        }
                    }
                    call(binding,lines)
                }
            }, {
                "连接超时，请确认网络状态是否联通".toast(activity)
            })
        } else {
            for (line in lineSets) {
                val ts = line.place(activity,null)
                lines.add(ts)
                binding.standardLl.addView(ts.viewBinding.root)
            }
            call(binding,lines)
        }
    }

    fun actionText(): String {
       return when(action.pageBottom){
            FormAction.Add-> return "添加"
            FormAction.Save-> return "保存"
            FormAction.Delete->return "删除"
           else-> return action.pageBottom.toString()
        }
    }

    fun check(activity: Activity,lines:MutableList<Parent<*, *>>,go:()->Unit) {
        for (r in lines){
            if (!r.check()){
                when(r.line.dataType){
                    DataType.Text->{
                        ( r.line as TextSet).hint.toast(activity)
                    }
                    DataType.Img->{
                        "请上传${( r.line as ImgSet).title}".toast(activity)
                    }
                }
                return
            }
        }
        go()
    }

    fun collectData(activity: Activity,lines:MutableList<Parent<*, *>>,call:(Boolean)->Unit): Unit {
        val load=activity.onLoad()
//        val result= mutableMapOf<String,Any>()
        for (r in lines){
            if (r.data()==null){

            }else{
                if (post?.loadUrl?.isNotEmpty()==true){
                   val param= post?.loadParams?.find { it.name== r.line.serviceName}
                    if (param==null){
                        post?.loadParams?.add(LoadParam(r.line.serviceName,r.data().toStringPro(),CacheType.None))
                    }else{
                        param.def=r.data().toStringPro()
                    }
                }
//                result[r.line.serviceName ]= r.data()!!
            }
        }
        Expand.http(post, {
            if (it.get<String>("code") == "200") {
                when (action.pageBottom) {
                    FormAction.Add -> "添加成功".toast(activity)
                    FormAction.Save -> "保存成功".toast(activity)
                    else -> "操作成功".toast(activity)
                }
                call(true)
            } else {
                it.get<String>("msg").toast(activity)
                call(false)
            }
            load?.dismiss()
        }, {
            load?.dismiss()
            "网络连接失败".toast(activity)
        })


    }
}