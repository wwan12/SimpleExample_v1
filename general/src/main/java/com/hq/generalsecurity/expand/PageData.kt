package com.hq.generalsecurity.expand

import com.hq.tool.http.Method
import java.io.Serializable
import java.util.ArrayList

import com.google.gson.JsonDeserializationContext

import com.google.gson.JsonElement

import com.google.gson.JsonDeserializer
import com.hq.tool.loge
import java.lang.reflect.Type



//data class GX(var code :String, var msg: String,var data:MutableList<GXData>)
//data class GXData(var id :String, var label: String,var children:MutableList<GXData>?)
/**
 * rule 数据正则
 * serviceName 后端名称
 */
data class PageSet(val version:VersionInfo, val load: UrlSet?,val post: UrlSet?, val pageType: PageType, val pageName:String, var actionType: FormAction, val lineSets: ArrayList<LineSet>,val extra:MutableMap<String,String>):Serializable

data class VersionInfo(val code:String,val name:String)

data class UrlSet(val loadUrl:String,val requestMethod: Method,val loadParams:ArrayList<LoadParam>):Serializable

data class LoadParam(val name:String,var def:String,val cache:CacheType)

open class LineSet(var dataType: DataType, var must:Boolean=false, var data:String="", var servicePath:ArrayList<String>?=null, var serviceName:String="", var onClick:ClickAction=ClickAction.None, var onLongClick:ClickAction=ClickAction.None):Serializable

data class DataSet(var title:String): LineSet(DataType.OnlyData)


data class TextSet(var type: TextType, var title:String, var hint:String, var rule: Rule?, var maxLength:Int, var options:ArrayList<Option>?): LineSet(
    DataType.Text
)

data class Option(val id:String,val title:String,val childOptions:ArrayList<Option>):Serializable

data class ImgSet(var title:String,var format:ImgFormat,var action:ClickAction): LineSet(DataType.Img)

enum class PageType{
    FormStandard,
    ListStandard,
    FreeStandard
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
    Delete
}



enum class TextType{
    Text,
    TextInput,
    TextSelect,
    TextCheck,
}

enum class DataType{
    OnlyData,
    Text,
    Img,
}
enum class Rule{
    None,
    Number,
    Phone,
    IdCard,
    Reg,
}
enum class ImgFormat{
    Base64,
    Stream
}

enum class ClickAction{
    None,
    Camera,
    Gallery,
    Total
}


