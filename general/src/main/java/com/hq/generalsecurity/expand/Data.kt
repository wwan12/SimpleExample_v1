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
data class PageSet(val ver:String, val urlSet: UrlSet?, val pageType: PageType, val pageName:String, var actionType: ActionType, val lineSets: ArrayList<LineSet>):Serializable

data class UrlSet(val loadUrl:String,val requestMethod: Method,val postUrl:String,val postMethod: Method):Serializable

open class LineSet(var dataType: DataType, var must:Boolean=false, var data:String="", var servicePath:ArrayList<String>?=null, var serviceName:String=""):Serializable

data class DataSet(var title:String): LineSet(DataType.OnlyData)


data class TextSet(var type: TextType, var title:String, var hint:String, var rule: Rule?, var maxLength:Int, var options:ArrayList<Option>?): LineSet(
    DataType.Text
)

data class Option(val id:String,val title:String,val childOptions:ArrayList<Option>):Serializable

data class ImgSet(var title:String,var format:ImgFormat,var option:ImgOption): LineSet(DataType.Img)

enum class PageType{
    FormStandard,
    ListStandard
}


enum class ActionType{
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
}
enum class ImgFormat{
    Base64,
    Stream
}

enum class ImgOption{
    None,
    Camera,
    Gallery,
    Total
}


