package com.hq.general.model

import com.google.gson.Gson
import com.hq.general.expand.Expand
import com.hq.tool.http.FailData
import com.hq.tool.loge
import java.util.ArrayList

data class ListStandardPage(val load: UrlInfo?, val rowName:String, val launchSearch:Boolean?, val onItemClick: ClickAction?,
                            val route: String?,val paging:Boolean?,val detailsPost:Boolean?, val lineSets: ArrayList<LineSet>,val itemLayoutName:String?) : PageSet(
    VersionInfo("1",""),
    null,
    PageType.ListStandard,
    "",
    PageAction()
){
    fun load(keyWord:String,page:Int, pageSize:Int, successCall:(ArrayList<MutableMap<String, Any>>)->Unit, failCall:(FailData)->Unit): Unit {
        val pageName= load!!.loadParams.find { it.name=="PageName" }
        if (pageName!=null){
            pageName.def=page.toString()
        }else{
            load.loadParams.add(LoadParam("page",page.toString(),"page", CacheType.None))
        }
        val pageSizeName= load.loadParams.find { it.name=="PageSizeName" }
        if (pageSizeName!=null){
            pageSizeName.def=pageSize.toString()
        }else{
            load.loadParams.add(LoadParam("pageSize","10","pageSize", CacheType.None))
        }
        val keyWordName= load!!.loadParams.find { it.name=="KeyWord" }
        if (keyWordName!=null){
            keyWordName.def=keyWord
        }else{
            load.loadParams.add(LoadParam("keyWord",keyWord,"keyWord", CacheType.None))
        }
        load.load({
            val list = it.tryGet<ArrayList<MutableMap<String, Any>>>(rowName)

            if (list != null) {
                successCall(list)
            } else {
                failCall(FailData(load.loadUrl, "No Data"))
            }
        },failCall)
    }


}