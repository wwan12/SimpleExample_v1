package com.hq.generalsecurity.expand

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.hq.generalsecurity.set.Flag
import com.hq.generalsecurity.set.Storage
import com.hq.generalsecurity.set.UrlSet
import com.hq.generalsecurity.standardform.ListActivity
import com.hq.generalsecurity.standardform.StandardFormActivity
import com.hq.generalsecurity.standardform.StandardFreeActivity
import com.hq.generalsecurity.standardform.ViewPagerActivity
import com.hq.generalsecurity.widget.form.*
import com.hq.tool.http.FailData
import com.hq.tool.http.Http
import com.hq.tool.http.SuccessData
import com.hq.tool.loge
import com.hq.tool.system.startActivity
import com.hq.zip.IZipCallback
import com.hq.zip.ZipManager
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
                     load(formPage(fileName,it))
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

    /**
     * 根据给定的类型名和字段名，返回R文件中的字段的值
     * @param typeName 属于哪个类别的属性 （id,layout,drawable,string,color,attr......）
     * @param fieldName 字段名 * @return 字段的值 * @throws Exception
     * */
    fun getFieldValue(typeName: String, fieldName: String, context: Context): Int {
        var i = -1
        i = try {
            val clazz = Class.forName(context.packageName.toString() + ".R$" + typeName)
            clazz.getField(fieldName).getInt(null)
        } catch (e: Exception) {
//            Log.d("" + context.getClass(),
//                "没有找到" + context.getPackageName()
//                    .toString() + ".R$" + typeName + "类型资源 " + fieldName + "请copy相应文件到对应的目录."
//            )
            return -1
        }
        return i
    }

    fun getImgPath(): String {
        return imgCachePath
    }
    fun setImgPath(path:String): Unit {
        imgCachePath=path
    }



    fun toFormActivity(activity: Activity,name: String): Unit {
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
            activity.startActivity(intent)
        }
    }
    inline fun <reified T:Activity> toActivity(activity: Activity, name: String): Unit {
        activity.getPage(name){
            val intent=Intent(activity,T::class.java)
            intent.putExtra(Flag.PAGE_FLAG,it)
            activity.startActivity(intent)
        }
    }

    fun http(info:UrlInfo?, success:(SuccessData)->Unit, fail:(FailData)->Unit): Unit {
        if(info!=null&&info.loadUrl!=""){
            Http.any(info.requestMethod){
                url= UrlSet.BASE_URL + info.loadUrl
                _headers= UrlSet.headers
                params(getLoadParams(info.loadParams))
                success(success)
                fail(fail)
            }
        }else{
            fail(FailData("",""))
        }
    }
    private fun getLoadParams(params:ArrayList<LoadParam>): MutableMap<String,Any> {
        val m= mutableMapOf<String,Any>()
        for (p in params){
            when(p.cache){
                CacheType.None-> m.put(p.name,p.def)
                CacheType.Local-> m.put(p.name,StorageExpand.getLocalData(p.name))
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
                    PageType.FreeStandard,
                    "",
                    PageAction(FormAction.None,FormAction.None)
                )
            }
//        }
        }
    }

}