package com.hq.general.model

import android.app.Activity
import com.hq.general.control.ControlBase
import com.hq.general.expand.Expand
import com.hq.general.expand.Expand.getPage
import com.hq.tool.http.Method
import java.io.Serializable
import java.util.ArrayList
import com.hq.general.expand.Expand.http
import com.hq.general.expand.StorageExpand
import com.hq.general.set.UrlSet
import com.hq.tool.http.FailData
import com.hq.tool.http.Http
import com.hq.tool.http.SuccessData
import com.hq.tool.loge
import com.hq.tool.misc.Reflect


/**
 * rule 数据正则
 * serviceName 后端名称
 */
open class PageSet(var version: VersionInfo,var control:Control?, var pageType: PageType, var pageName:String, var action: PageAction):Serializable


data class FreeStandardPage(val load: UrlInfo?, val lineSets: ArrayList<LineSet>) : PageSet(
    VersionInfo("1",""),
    null,
    PageType.FreeStandard,
    "",
    PageAction()
)

data class ViewPagerStandardPage(val pageNames: ArrayList<Link>) : PageSet(
    VersionInfo("1",""),
    null,
    PageType.ViewPagerStandard,
    "",
    PageAction()
)

data class VersionInfo(val code:String,val name:String):Serializable

data class Control(val name:String):Serializable{
    fun getControl(activity: Activity,pageSet:PageSet): ControlBase<*> {
       return Reflect.getEntity<ControlBase<*>>(name,activity,pageSet)
    }
}

data class PageAction(val barRight: FormAction?=null, val pageBottom: FormAction?=null,val pageBottom2: FormAction?=null):Serializable

data class UrlInfo(val loadUrl:String,val requestMethod: Method,val loadParams:ArrayList<LoadParam>,var cache: CacheType?):Serializable
{
    fun load(success:(SuccessData)->Unit,fail:(FailData)->Unit): Unit {
        if (cache==null){
           cache= when{
                loadUrl.isEmpty()->CacheType.None
                loadUrl.contains("/")->CacheType.Net
//                loadUrl.contains("/")->CacheType.Net
               else->CacheType.Local
            }
        }

        when (cache){
            CacheType.None->{
                success(SuccessData(loadUrl, mutableMapOf()))
            }
            CacheType.Local->{

               val map= StorageExpand.getLocalData<MutableMap<String,Any>>(loadUrl)
                if (map!=null){
                    map["code"]="200"
                    success(SuccessData(loadUrl,map))
                }else{
                    fail(FailData(loadUrl,"数据未初始化"))
                }
            }
            CacheType.Net->{
                Expand.http(this,success,fail)
            }
        }

    }
}

data class LoadParam(val name:String,var def:Any,val apiName:String,val cache:CacheType?):Serializable

data class FormAction(val name: String,val route: String,val isCheck:Boolean=false,val isClose:Boolean=false,val type:PageActionType=PageActionType.None):Serializable

/**
 * 页面链接
 */
data class Link(val name: String,val route: String):Serializable{
    fun getPage(activity: Activity,load:(PageSet?)->Unit) {
        activity.getPage(route,load)
    }
}

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

enum class PageActionType{
    None,
    Update,
    Dialog,
    PopWindow,
    Go
}






