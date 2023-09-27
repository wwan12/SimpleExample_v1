package com.hq.generalsecurity.standardform


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.viewbinding.ViewBinding
import com.hq.generalsecurity.*
import com.google.gson.Gson
import com.hq.generalsecurity.databinding.ActivityStandardFormBinding
import com.hq.generalsecurity.expand.*
import com.hq.generalsecurity.formwidget.*
import com.hq.tool.http.Http

import com.hq.tool.onLoad
import com.hq.tool.toStringPro
import com.hq.tool.toast
import okhttp3.Headers

class StandardFormActivity:BaseActivity<ActivityStandardFormBinding>() {


//    val result= mutableMapOf<String,Any>()
//    val resultCheck= mutableListOf<String>()
    val lines= mutableListOf<Parent<*,*>>()

    lateinit var pageSet: PageSet

    override fun getBinding(): ActivityStandardFormBinding {
        return  ActivityStandardFormBinding.inflate(layoutInflater)
    }

    override fun initView() {
        pageSet=intent.getSerializableExtra(PageFlag) as PageSet
        viewBinding.bar.back.setOnClickListener { finish() }
        viewBinding.bar.title.text=pageSet.pageName
        var actionText=""
        when(pageSet.actionType){
            FormAction.Add->actionText="添加"
            FormAction.Save->actionText="保存"
        }
        if (actionText.isEmpty()){
            viewBinding.standardActionButton.visibility= View.GONE
        }else{
            viewBinding.standardActionButton.text=actionText
            viewBinding.standardActionButton.setOnClickListener {
                check{
                    collectData()
                }
            }
        }
        http(pageSet.load,{
            if (it.get<String>("code")=="200"){
                for (line in pageSet.lineSets ){
                    if (line.serviceName.isNotEmpty()){
                        line.data=it.tryGet<String>(line.serviceName).toStringPro()
                    }
                    var view:View?=null
                    when(line.dataType){
                        DataType.OnlyData->{
                            val od= OnlyData()
                            od.line=line
                            lines.add(od)
                        }
                        DataType.Text->{
                            val ts= placeText(line as TextSet)
                            lines.add(ts)
                            view=  ts.viewBinding.root
                        }
                        DataType.Img->{
                            val img=placeImg(line as ImgSet)
                            lines.add(img)
                            view=  img.viewBinding.root
                        }
                    }
                    if (view!=null){
                        if (line.serviceName.isNotEmpty()){
                            view?.tag="${line.dataType}:${line.servicePath}:${line.serviceName}"
                        }
                        viewBinding.standardLl.addView(view)
                    }
                }
            }
        },{
            if (it.url.isNotEmpty()){
                "连接超时，请确认网络状态是否联通".toast(this@StandardFormActivity)
            }else{
                for (line in pageSet.lineSets ){
                    var view:View?=null
                    when(line.dataType){
                        DataType.Text->{
                            val ts= placeText(line as TextSet)
                            lines.add(ts)
                            view=  ts.viewBinding.root
                        }
                        DataType.Img->{
                            val img=placeImg(line as ImgSet)
                            lines.add(img)
                            view=  img.viewBinding.root
                        }
                    }
                    viewBinding.standardLl.addView(view)
                }
            }
        })
    }


//    private fun gx(): Unit {
//        //目前三级菜单等待之后去掉
//        val gxView=  standard_ll.get(standard_ll.childCount-1).findViewById<TextView>(R.id.text_content)
//        Expand.initManager(this,{
//            gxView.tag=gxView.text.toString()
//            gxView.text= Expand.getManagerName(gxView.text.toString())
//        },{id,text->
//            gxView.text=text
//            gxView.tag=id
//        })
//        standard_ll.get(standard_ll.childCount-1).findViewById<TextView>(R.id.text_content).setOnClickListener {
//            Expand.openManagerPop(this)
//        }
//    }




    private fun check(go:()->Unit) {
        for (r in lines){
            if (!r.check()){
                when(r.line.dataType){
                    DataType.Text->{
                        ( r.line as TextSet).hint.toast(this)
                    }
                    DataType.Img->{
                        "请上传${( r.line as ImgSet).title}".toast(this)
                    }
                }
               return
            }
        }
        go()
    }

    private fun collectData(): Unit {
        val load=onLoad()
        val result= mutableMapOf<String,Any>()
        for (r in lines){
            if (r.data()==null){

            }else{
                result[r.line.serviceName ]= r.data()!!
            }

        }

        http(pageSet.post,{
            if (it.get<String>("code")=="200"){
                when(pageSet.actionType){
                    FormAction.Add->"添加成功".toast(this@StandardFormActivity)
                    FormAction.Save->"保存成功".toast(this@StandardFormActivity)
                    else->"操作成功".toast(this@StandardFormActivity)
                }
//                    it.get<String>("msg").toast(this@StandardFormActivity)
                finish()
            }else{
                it.get<String>("msg").toast(this@StandardFormActivity)
            }
            load?.dismiss()
        },{
            load?.dismiss()
            "网络连接失败".toast(this@StandardFormActivity)
        })


    }



}