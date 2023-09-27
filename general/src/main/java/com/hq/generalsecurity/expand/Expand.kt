package com.hq.generalsecurity.expand

import android.annotation.SuppressLint
import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.viewbinding.ViewBinding
import com.hq.generalsecurity.adapter.GxAdapter
import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.hq.generalsecurity.R
import com.hq.generalsecurity.formwidget.Text
import com.hq.generalsecurity.formwidget.TextCheck
import com.hq.generalsecurity.formwidget.TextInput
import com.hq.generalsecurity.formwidget.TextSelect
import com.hq.tool.loge
import com.hq.tool.widget.openAnyViewWindowCenter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type


@SuppressLint("StaticFieldLeak")
object Expand {
//    private var gxAdapter :GxAdapter?=null
//
//    private var pop: PopupWindow?=null

    private  val hierarchy= Hierarchy()

    private  var imgCachePath= ""

    private var main:Activity?=null


    private val pages= mutableMapOf<String, PageSet>()

    fun Activity.getPage(fileName:String): PageSet {
        return if (pages.containsKey(fileName)){
            pages[fileName]!!
        }else{
            val inputReader= InputStreamReader(assets.open(fileName))
            val bufReader = BufferedReader(inputReader)
            var line:String?=""
            var result=""
            while((bufReader.readLine().also { line = it }) != null)
                result += line

            val gb = GsonBuilder()
            gb.registerTypeAdapter(LineSet::class.java, LineSetDeserializer())
            val customGson = gb.create()
            customGson.fromJson(result, PageSet::class.java)
        }

    }

    fun getImgPath(): String {
        return imgCachePath
    }
    fun setImgPath(path:String): Unit {
        imgCachePath=path
    }

    fun getMain(): Activity {
        return main!!
    }
    fun setMain(main:Activity): Unit {
        this.main=main
    }
//    fun initManager(activity: Activity,loadCall:()->Unit,selectCall:(String,String)->Unit): Unit {
//        Api.getManager({
//            gxAdapter = GxAdapter(activity as AppCompatActivity) { data ->
//                if (data.children == null || data.children!!.size == 0) {
//                    pop?.dismiss()
//                    pop = null
//                    selectCall(data.id, data.label)
//                }
//            }
//            gxAdapter?.init(it)
//            for (f in it.data) {
//                if (f.children != null) {
//                    for (f1 in f.children!!) {
//                        hierarchy.firstList.add(f1.label)
//                        hierarchy.thirdIds[f1.label] = f1.id
//                        getHierarchyChildren(f1, hierarchy.secondList) { t ->
//                            getHierarchyChildren(t, hierarchy.thirdList) {
//                            }
//                        }
//                    }
//                }
//            }
//            loadCall()
//
//        }, {
//
//        })
//    }

//    fun openManagerPop(activity: Activity): Unit {
//        pop = activity.openAnyViewWindowCenter(
//            R.layout.pop_list_gx, LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM)
//        pop?.contentView?.gx_close?.setOnClickListener { pop?.dismiss() }
//        pop?.contentView?.gx_list?.adapter= gxAdapter
//        gxAdapter?.open()
//    }
    fun getManagerName(sysOrgCode:String): String {
        for (th in hierarchy.thirdIds) {
            if (th.value == sysOrgCode) {
               return th.key
            }
        }
        return ""
    }
//
//    private fun getHierarchyChildren(gxData: GXData, map: MutableMap<String, MutableList<String>>, children:(GXData)->Unit): Unit {
//        val tlist=mutableListOf<String>()
//        for (t in gxData.children!!){
//            tlist.add(t.label)
//            if (t.children!=null){
//                children(t)
//            }
//            hierarchy.thirdIds[t.label]=t.id
//        }
//        map[gxData.label]=tlist
//    }


    private class Hierarchy(): LinkageProvider {

        val firstList= mutableListOf<String>()

        val secondList= mutableMapOf<String,MutableList<String>>()
        val thirdList=mutableMapOf<String,MutableList<String>>()

        val thirdIds= mutableMapOf<String,String>()

        override fun firstLevelVisible(): Boolean {
            return true
        }

        override fun thirdLevelVisible(): Boolean {
            return true
        }

        override fun provideFirstData(): MutableList<*> {
            return firstList
        }

        override fun linkageSecondData(firstIndex: Int): MutableList<*> {
            return secondList[firstList[firstIndex]]!!
        }

        override fun linkageThirdData(firstIndex: Int, secondIndex: Int): MutableList<*> {
            val s= secondList[firstList[firstIndex]]!![secondIndex]
            return thirdList[s]!!
        }

        override fun findFirstIndex(firstValue: Any?): Int {
            return firstList.indexOf(firstValue.toString())
        }

        override fun findSecondIndex(firstIndex: Int, secondValue: Any?): Int {
            return secondList[firstList[firstIndex]]!!.indexOf(secondValue)
        }

        override fun findThirdIndex(firstIndex: Int, secondIndex: Int, thirdValue: Any?): Int {
            val s= secondList[firstList[firstIndex]]!![secondIndex]
            return thirdList[s]!!.indexOf(thirdValue)
        }
    }


    /**
     * GSON的解析自适应程序
     */
    class LineSetDeserializer : JsonDeserializer<LineSet> {
        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext): LineSet {
//        return if (json == null){
//            null
//        } else {
            // null management can be improved
            val dataType = json!!.asJsonObject.get("dataType").asString
            dataType.loge("dataType")
            json.toString().loge("totJson")
            return when (dataType) {
                DataType.OnlyData.toString() -> context.deserialize(json, DataSet::class.java)
                DataType.Text.toString() -> context.deserialize(json, TextSet::class.java)
                DataType.Img.toString()-> context.deserialize(json, ImgSet::class.java)
                else-> LineSet(DataType.OnlyData)
            }
//        }
        }
    }



}