package com.hq.general.model

import android.app.Activity
import com.hq.general.extraction.DataExtraction
import com.hq.general.expand.Expand
import com.hq.general.set.Flag
import com.hq.general.widget.form.*
import com.hq.general.widget.group.LineGroup
import com.hq.general.widget.group.SelectGroup
import com.hq.general.widget.group.UserIcon
import com.hq.general.widget.img.Img
import com.hq.general.widget.img.ImgBar
import com.hq.general.widget.img.ImgHideList
import com.hq.general.widget.img.ImgHideSingle
import com.hq.general.widget.text.*
import java.io.Serializable
import java.util.ArrayList

open class LineSet(var dataType: DataType, var name:String="", var title:String="", var must:Boolean=false, var data:String="", var servicePath: ArrayList<String>?=null, var serviceName:String="",
                   var onClick: ClickAction? = null, var onLongClick: ClickAction ? = null,
                   var source:CacheType?=CacheType.None, val position: Flag.Dir? = null, var scriptPath:String="",
                   val route: String?=null, var theme: Theme?=null):
    Serializable
{
    open fun place(activity: Activity,data: DataExtraction): Parent<*, *> {
        return  Text()
    }
}

data class DataSet(var update:Boolean): LineSet(DataType.OnlyData){
    override fun place(activity: Activity, data: DataExtraction): Parent<*, *> {
        val od=OnlyData()
        od.viewBinding = Expand.getViewBinding(activity, od.getViewBindingCls())
        od.line=this
        od.data=data
        od.init()
        return od
    }
}

data class GroupSet(val childs:ArrayList<LineSet> , val type: GroupType): LineSet(DataType.Group){
    override fun place(activity: Activity,data: DataExtraction): Parent<*, *> {
        val group = when(type){
            GroupType.Line -> {
                val group= LineGroup()
                group.viewBinding = Expand.getViewBinding(activity, group.getViewBindingCls())
                group
            }
            GroupType.UserIcon -> {
                val group= UserIcon()
                group.viewBinding = Expand.getViewBinding(activity, group.getViewBindingCls())
                group
            }
            GroupType.SelectGroup -> {
                val group= SelectGroup()
                group.viewBinding = Expand.getViewBinding(activity, group.getViewBindingCls())
                group
            }
        }
        group.line=this
        group.data=data
        group.init()
        return  group
    }
}


data class TextSet(var type: TextType, var hint:String, var rule: Rule?, var maxLength:Int, val bind:String?=null,var options:ArrayList<Option>?): LineSet(
     DataType.Text
){
    /**
     * 解析Text标签
     */
    override fun place(activity: Activity,data: DataExtraction): Parent<*, *> {
        val text = when(type){
            TextType.Text -> {
                val text= Text()
                text.viewBinding = Expand.getViewBinding(activity, text.getViewBindingCls())
                text
            }
            TextType.TextInput -> {
                val text= TextInput()
                text.viewBinding = Expand.getViewBinding(activity, text.getViewBindingCls())
                text
            }
            TextType.TextSelect -> {
                val text= TextSelect()
                text.viewBinding = Expand.getViewBinding(activity, text.getViewBindingCls())
                text
            }
            TextType.TextCheck -> {
                val text= TextCheck()
                text.viewBinding = Expand.getViewBinding(activity, text.getViewBindingCls())
                text
            }
            TextType.TextMulti -> {
                val text= TextMultistage()
                text.viewBinding = Expand.getViewBinding(activity, text.getViewBindingCls())
                text
            }
        }
        text.line=this
        if (theme!=null){
            theme?.setTheme(text)
        }
        text.data=data
        text.init()
        return text
    }
}

data class Option(val id:String,val title:String,val childOptions:ArrayList<Option>?):Serializable
//count==-1 无限
data class ImgSet(var format: FileFormat, val placeholder: CacheType, val placeholderName:String, val cache: CacheType,
                  val type: ImgType, val list: ListSet
): LineSet(DataType.Img) {
    /**
     * 解析Img标签
     */
    override fun place(activity: Activity,data: DataExtraction): Parent<*, *> {
        val img = when (type) {
            ImgType.Icon -> {
                val img = Img()
                img.viewBinding = Expand.getViewBinding(activity, img.getViewBindingCls())
                img
            }
            ImgType.HideSingle -> {
                val img = ImgHideSingle()
                img.viewBinding = Expand.getViewBinding(activity, img.getViewBindingCls())
                img
            }
            ImgType.HideList -> {
                val img = ImgHideList()
                img.viewBinding = Expand.getViewBinding(activity, img.getViewBindingCls())
                img
            }
            ImgType.Bar -> {
                val img = ImgBar()
                img.viewBinding = Expand.getViewBinding(activity, img.getViewBindingCls())
                img
            }
        }
        img.line = this
        img.data=data
        img.init()
        return img
    }
}

data class ListSet(val model: ListModel, val minCount:Int, val maxCount:Int)

enum class Rule{
    None,
    Number,
    Phone,
    IdCard,
    Reg,
}
enum class FileFormat{
    Base64,
    Stream
}
enum class ImgType{
    Icon,
    HideSingle,
    HideList,
    Bar
}

enum class ListModel{
    Single,
    Fixed,
    Grow,
    Infinite
}

enum class ClickAction{
    None,
    Camera,
    Gallery,
    CameraGallery,
    Time,
    Date,
    DateTime,
    File,
    Script,
    //跳转
    Go,

}
enum class TextType{
    Text,
    TextInput,
    TextSelect,
    TextCheck,
    TextMulti,
}

enum class DataType{
    OnlyData,
    Group,
    Text,
    Img,
    File,
}

enum class GroupType{
    Line,
    UserIcon,
    SelectGroup
}
