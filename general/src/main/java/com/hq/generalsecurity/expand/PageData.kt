package com.hq.generalsecurity.expand

import com.hq.tool.http.Method
import java.io.Serializable
import java.util.ArrayList
import com.hq.generalsecurity.expand.Expand.http
import com.hq.tool.http.FailData


/**
 * rule 数据正则
 * serviceName 后端名称
 */
open class PageSet(var version:VersionInfo, var pageType: PageType, var pageName:String, var action: PageAction,var theme: Theme?=null):Serializable


data class ListStandardPage( val load: UrlInfo?,val rowName:String,val onItemClick:ClickAction,val route: String,val lineSets: ArrayList<LineSet>) : PageSet(
    VersionInfo("1",""),
    PageType.ListStandard,
    "",
    PageAction(FormAction.None,FormAction.None)
){
    fun load(page:Int,pageSize:Int,successCall:(ArrayList<MutableMap<String, Any>>)->Unit,failCall:(FailData)->Unit): Unit {
        val pageName= load!!.loadParams.find { it.name=="PageName" }
        if (pageName!=null){
            pageName.def=page.toString()
        }else{
            load.loadParams.add(LoadParam("page",page.toString(),CacheType.None))
        }
        val pageSizeName= load.loadParams.find { it.name=="PageSizeName" }
        if (pageSizeName!=null){
            pageSizeName.def=pageSize.toString()
        }else{
            load.loadParams.add(LoadParam("pageSize","10",CacheType.None))
        }
        http(load, {
            val list = it.tryGet<ArrayList<MutableMap<String, Any>>>(rowName)
            if (list!=null){
                successCall(list)
            }else{
                failCall(FailData(load.loadUrl,"No Data"))
            }
        },failCall)
    }
}

data class FreeStandardPage( val load: UrlInfo?,val lineSets: ArrayList<LineSet>) : PageSet(
    VersionInfo("1",""),
    PageType.FreeStandard,
    "",
    PageAction(FormAction.None,FormAction.None)
)

data class ViewPagerStandardPage(val pageNames: ArrayList<Link>) : PageSet(
    VersionInfo("1",""),
    PageType.ViewPagerStandard,
    "",
    PageAction(FormAction.None,FormAction.None)
)

data class VersionInfo(val code:String,val name:String):Serializable

data class PageAction(val barRight:FormAction,val pageBottom:FormAction):Serializable

data class UrlInfo(val loadUrl:String,val requestMethod: Method,val loadParams:ArrayList<LoadParam>):Serializable

data class LoadParam(val name:String,var def:String,val cache:CacheType):Serializable

data class Link(val pageName: String):Serializable

enum class PageType{
    FormStandard,
    ListStandard,
    FreeStandard,
    ViewPagerStandard
}

enum class CacheType{
    None,
    Local,
    Net
}

enum class FormAction{
    None,
    Add,
    Save,
    Change,
    Delete,
    Go
}






