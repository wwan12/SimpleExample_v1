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

    companion object{
      const val PageSet="PageSet"
    }

//    val result= mutableMapOf<String,Any>()
//    val resultCheck= mutableListOf<String>()
    val lines= mutableListOf<Parent<*,*>>()

    lateinit var pageSet: PageSet

    override fun getBinding(): ActivityStandardFormBinding {
        return  ActivityStandardFormBinding.inflate(layoutInflater)
    }

    override fun initView() {
        pageSet=intent.getSerializableExtra(PageSet) as PageSet
        viewBinding.bar.back.setOnClickListener { finish() }
        viewBinding.bar.title.text=pageSet.pageName
        var actionText=""
        when(pageSet.actionType){
            ActionType.Add->actionText="添加"
            ActionType.Save->actionText="保存"
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
        if (pageSet.urlSet!=null){
            Http.any(pageSet.urlSet!!.requestMethod){
                url= BASE_URL + pageSet.urlSet!!.loadUrl
                _headers= Headers.headersOf("Authorization","Bearer ${user?.token}")
                success {
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
                                }
                                DataType.Text->{
                                    val textSet=line as TextSet
                                    view=  placeText(textSet)
                                }
                                DataType.Img->{
                                    val img=Img()
                                    img.viewBinding = getViewBinding(img.getViewBindingCls())
                                    img.line=line as ImgSet
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
//                        gx()
                    }
                }
                fail {
                    "连接超时，请确认网络状态是否联通".toast(this@StandardFormActivity)
                }
            }
        }else {
            for (line in pageSet.lineSets ){
                var view:View?=null
                when(line.dataType){
                    DataType.Text->{
                        val textSet=line as TextSet
                        view=  placeText(textSet)
                    }
                    DataType.Img->{}
                }
               viewBinding.standardLl.addView(view)
            }
        }


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
  private fun <T:ViewBinding> getViewBinding(cls:Class<T>): T {
    val inflate = cls.getDeclaredMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.javaPrimitiveType
    )
    val viewBinding = inflate.invoke(null, layoutInflater, parent, false)
    return viewBinding as T
  }

    private fun placeText(textSet: TextSet): View {
        val text = when(textSet.type){
            TextType.Text-> {
                val text= Text()
                text.viewBinding = getViewBinding(text.getViewBindingCls())
                text
            }
            TextType.TextInput-> {
                val text= TextInput()
                text.viewBinding = getViewBinding(text.getViewBindingCls())
                text
            }
            TextType.TextSelect-> {
                val text= TextSelect()
                text.viewBinding = getViewBinding(text.getViewBindingCls())
                text
            }
            TextType.TextCheck-> {
                val text= TextCheck()
                text.viewBinding = getViewBinding(text.getViewBindingCls())
                text
            }
        }

        text.line=textSet


        return text.viewBinding.root
    }

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

        Http.any(pageSet.urlSet!!.postMethod){
            url= BASE_URL +pageSet.urlSet!!.postUrl

            _headers= Headers.headersOf("Authorization","Bearer ${user?.token}")

            "json"-Gson().toJson(result)

            success {
                if (it.get<String>("code")=="200"){
                    when(pageSet.actionType){
                        ActionType.Add->"添加成功".toast(this@StandardFormActivity)
                        ActionType.Save->"保存成功".toast(this@StandardFormActivity)
                        else->"操作成功".toast(this@StandardFormActivity)
                    }
//                    it.get<String>("msg").toast(this@StandardFormActivity)
                    finish()
                }else{
                    it.get<String>("msg").toast(this@StandardFormActivity)
                }
                load?.dismiss()
            }
            fail {
                load?.dismiss()
                "网络连接失败".toast(this@StandardFormActivity)
            }
        }


    }



}