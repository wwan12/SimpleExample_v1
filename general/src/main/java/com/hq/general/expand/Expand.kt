package com.hq.general.expand

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.PopupWindow
import androidx.viewbinding.ViewBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.hq.general.control.ControlBase
import com.hq.general.debug.TestDataCreator
import com.hq.general.model.*
import com.hq.general.set.Flag
import com.hq.general.set.Storage
import com.hq.general.set.UrlSet
import com.hq.general.standardform.ListActivity
import com.hq.general.standardform.StandardFormActivity
import com.hq.general.standardform.StandardFreeActivity
import com.hq.general.standardform.ViewPagerActivity
import com.hq.general.widget.form.Parent
import com.hq.tool.http.FailData
import com.hq.tool.http.Http
import com.hq.tool.http.Submit
import com.hq.tool.http.SuccessData
import com.hq.tool.loge
import com.hq.tool.misc.Reflect
import com.hq.tool.toStringPro
import com.hq.tool.widget.openAnyViewWindowCenter
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.reflect.Type


object Expand {

    private  var imgCachePath= ""

    private val pages= mutableMapOf<String, PageSet>()

    fun Activity.getJson(fileName:String,load:(String?)->Unit): Unit {
        if (fileName.contains("http")){
            Http.download{
                url=fileName
                val ss=fileName.split("/")
                downloadPath="${cacheDir}/${ss[ss.size-1]}"
                success {
                    load( File(downloadPath).readText()) }
                fail { load(null) }
            }
        }else{
            val file = File("${Storage.cachePath}/Page/${fileName}.json")
            if (file.exists()){
                load(file.readText())
            }else{
                try {
                    var result=""
                    val inputReader= InputStreamReader(assets.open("${fileName}.json"))
                    val bufReader = BufferedReader(inputReader)
                    var line:String?=""
                    while((bufReader.readLine().also { line = it }) != null)
                        result += line
                    load(result)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    fun Activity.getPage(fileName:String,load:(PageSet?)->Unit) {
         if (pages.containsKey(fileName)){
             load(pages[fileName])
        }else{
             getJson(fileName){
                 if (it==null){
                     load(null)
                 }else{
                     val page=formPage(fileName,it)
                     if (UrlSet.isDebug){
                        addTestData(page)
                     }
                     load(page)
                 }
             }
        }
    }

    fun formPage(fileName:String,result:String): PageSet {
        val gb = GsonBuilder()
        gb.registerTypeAdapter(LineSet::class.java, LineSetDeserializer())
        gb.registerTypeAdapter(PageSet::class.java, PageSetDeserializer())
        val customGson = gb.create()
        val page= customGson.fromJson(result, PageSet::class.java)
        pages[fileName]=page
        return page
    }

    private fun addTestData(page:PageSet){
        Http.test{
            when(page.pageType){
                PageType.ListStandard->{
                    put(UrlSet.BASE_URL+(page as ListStandardPage).load?.loadUrl, Submit.TestResult(200, "",
                        TestDataCreator.createListData(page as ListStandardPage),200L))
                }
                PageType.FormStandard->{
                    put(UrlSet.BASE_URL+(page as FormStandardPage).load?.loadUrl, Submit.TestResult(200, "",
                        TestDataCreator.createFromData(page as FormStandardPage),200L))
                }
                else->{}
            }

        }
    }


    fun getImgPath(): String {
        return imgCachePath
    }
    fun setImgPath(path:String): Unit {
        imgCachePath=path
    }

    fun toFormActivity(activity: Activity,page: PageSet): Unit {
        val intent=  when(page.pageType){
            PageType.FreeStandard->Intent(activity, StandardFreeActivity::class.java)
            PageType.FormStandard->Intent(activity,StandardFormActivity::class.java)
            PageType.ListStandard->Intent(activity,ListActivity::class.java)
            PageType.ViewPagerStandard->Intent(activity,ViewPagerActivity::class.java)
            else -> Intent(activity,StandardFormActivity::class.java)
        }
        //"${name}->${it?.pageType}".loge()
        intent.putExtra(Flag.PAGE_FLAG,page)
        activity.startActivityForResult(intent,0)
    }

    fun toFormActivity(activity: Activity,name: String,call:(Intent)->Unit={}): Unit {
        activity.getPage(name){
            val intent=  when(it?.pageType){
                PageType.FreeStandard->Intent(activity, StandardFreeActivity::class.java)
                PageType.FormStandard->Intent(activity,StandardFormActivity::class.java)
                PageType.ListStandard->Intent(activity,ListActivity::class.java)
                PageType.ViewPagerStandard->Intent(activity,ViewPagerActivity::class.java)
                else -> Intent(activity,StandardFormActivity::class.java)
            }
            "${name}->${it?.pageType}".loge()
            intent.putExtra(Flag.PAGE_FLAG,it)
            call(intent)
            activity.startActivityForResult(intent,0)
        }
    }
    inline fun <reified T:Activity> toActivity(activity: Activity, name: String): Unit {
        activity.getPage(name){
            val intent=Intent(activity,T::class.java)
            intent.putExtra(Flag.PAGE_FLAG,it)
            activity.startActivityForResult(intent,0)
        }
    }

    fun http(info: UrlInfo?, success:(SuccessData)->Unit, fail:(FailData)->Unit): Unit {
        if(info!=null&&info.loadUrl!=""){
            Http.any(info.requestMethod){
                url= UrlSet.BASE_URL + info.loadUrl
                _headers= UrlSet.headers
                params(getLoadParams(info.loadParams))
                success(success)
                fail(fail)
            }
        }else{
            fail(FailData("",255,"未设置请求"))
        }
    }
    private fun getLoadParams(params:ArrayList<LoadParam>): MutableMap<String,Any> {
        val m= mutableMapOf<String,Any>()
        for (p in params){
            when(p.cache){
                CacheType.Local-> m[p.apiName] = StorageExpand.getLocalData<String>(p.name).toStringPro()
                else->{
                    m[p.apiName] = p.def
                }
//                CacheType.Net-> m.put(p.name,getLocalData(p.name))
            }

        }
        return  m
    }

    /**
     *
     */
    fun <T: ViewBinding> getViewBinding(activity: Activity,cls:Class<T>): T {
        val inflate = cls.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.javaPrimitiveType
        )
        val viewBinding = inflate.invoke(null, activity.layoutInflater, activity.parent, false)
        return viewBinding as T
    }


    fun Activity.setFromAction(button: View, pageSet: FormStandardPage, lines:MutableList<Parent<*, *>>, formAction: FormAction,control:ControlBase<*>?): Unit {
        button.setOnClickListener {
            when(formAction.type){
                PageActionType.Update-> {
                    if (formAction.isCheck){
                        pageSet.check(this,lines){
                            pageSet.collectData(lines)
                            if (control!=null){
                                control.onActionClick(formAction){
                                    pageSet.upload(this){
                                        if (it&&formAction.isClose){
                                            finish()
                                        }
                                    }
                                }
                            }else{
                                pageSet.upload(this){
                                    if (it&&formAction.isClose){
                                        finish()
                                    }
                                }
                            }


                        }
                    }else{
                        pageSet.collectData(lines)
                        if (control!=null){
                            control.onActionClick(formAction){
                                pageSet.upload(this){
                                    if (it&&formAction.isClose){
                                        finish()
                                    }
                                }
                            }
                        }else{
                            pageSet.upload(this){
                                if (it&&formAction.isClose){
                                    finish()
                                }
                            }
                        }


                    }
                }
                PageActionType.Go->  {
                    if (formAction.route.contains("Activity")){
                        Reflect.startActivity(this,formAction.route)
                    }else{
                        Expand.toFormActivity(this, formAction.route)
                    }
                    if (formAction.isClose){
                        finish()
                    }
                }
                PageActionType.Dialog-> {
                    val dialog= Reflect.getEntity<Dialog>(formAction.route,this)
                    dialog.setCancelable(false)
                    dialog.show()
                }
                PageActionType.PopWindow->{
                    val pop= Reflect.getEntity<PopupWindow>(formAction.route,this)
                    pop.isOutsideTouchable = true
                    pop.isFocusable = true
                    pop.contentView.setOnKeyListener { view, i, keyEvent ->
                        if (i == KeyEvent.KEYCODE_BACK) {//返回自动关闭pop
                            if (pop.isShowing) {
                                pop.dismiss();
                                return@setOnKeyListener true
                            }
                        }
                        return@setOnKeyListener false
                    }
                    pop.setOnDismissListener {
                        val params =window.attributes
                        params.alpha = 1f
                        window.attributes = params
                    }
                    val params = window.attributes
                    params.alpha = 0.7f
                    window.attributes = params
                    pop.showAtLocation(button, Gravity.BOTTOM,0,0)
                }
                else-> {

                }
            }

        }
    }



    /**
     * GSON的解析LineSet自适应程序
     */
    private class LineSetDeserializer : JsonDeserializer<LineSet> {
        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext): LineSet {
//        return if (json == null){
//            null
//        } else {
            // null management can be improved
            val dataType = json!!.asJsonObject.get("dataType").asString
            json.toString().loge("toJson")
            return when (dataType) {
                DataType.OnlyData.toString() -> context.deserialize(json, DataSet::class.java)
                DataType.Group.toString() -> context.deserialize(json, GroupSet::class.java)
                DataType.Text.toString() -> context.deserialize(json, TextSet::class.java)
                DataType.Img.toString()-> context.deserialize(json, ImgSet::class.java)
                DataType.File.toString()-> context.deserialize(json, FileSet::class.java)
                else-> LineSet(DataType.OnlyData)
            }
//        }
        }
    }

    /**
     * GSON的解析PageSet自适应程序
     */
    private class PageSetDeserializer : JsonDeserializer<PageSet> {
        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext): PageSet {
            val dataType = json!!.asJsonObject.get("pageType").asString
            return when (dataType) {
                PageType.FormStandard .toString() -> context.deserialize(json, FormStandardPage::class.java)
                PageType.ListStandard.toString() -> context.deserialize(json, ListStandardPage::class.java)
                PageType.ViewPagerStandard.toString()-> context.deserialize(json, ViewPagerStandardPage::class.java)
                PageType.FreeStandard.toString()-> context.deserialize(json, FreeStandardPage::class.java)
                else-> PageSet(
                    VersionInfo("1",""),
                    null,
                    PageType.FreeStandard,
                    "",
                    PageAction()
                )
            }
//        }
        }
    }

}