package com.hq.generalsecurity.expand

import android.app.Activity
import com.hq.generalsecurity.set.Flag
import com.hq.generalsecurity.widget.form.*
import com.hq.generalsecurity.widget.group.Group
import com.hq.generalsecurity.widget.img.Img
import com.hq.generalsecurity.widget.img.ImgBar
import com.hq.generalsecurity.widget.img.ImgHideList
import com.hq.generalsecurity.widget.img.ImgHideSingle
import com.hq.generalsecurity.widget.text.Text
import com.hq.generalsecurity.widget.text.TextCheck
import com.hq.generalsecurity.widget.text.TextInput
import com.hq.generalsecurity.widget.text.TextSelect
import java.io.Serializable
import java.util.ArrayList

open class LineSet(var dataType: DataType, var title:String="", var must:Boolean=false, var data:String="", var servicePath: ArrayList<String>?=null, var serviceName:String="",
                   var onClick:ClickAction=ClickAction.None, var onLongClick:ClickAction=ClickAction.None, val position: Flag.Dir= Flag.Dir.Left, var scriptPath:String="", var theme: Theme?=null):
    Serializable
{
    open fun place(activity: Activity,data:MutableMap<String,Any>?): Parent<*, *> {
        return  Text()
    }
}

data class DataSet(var update:Boolean): LineSet(DataType.OnlyData)

data class GroupSet(val childs:ArrayList<LineSet> ): LineSet(DataType.OnlyData){
    override fun place(activity: Activity,data:MutableMap<String,Any>?): Parent<*, *> {
        val group=Group()
        group.viewBinding = Expand.getViewBinding(activity,group.getViewBindingCls())
        group.line=this
        group.init(data)
        return  group
    }
}


data class TextSet(var type: TextType, var hint:String, var rule: Rule?, var maxLength:Int, var options:ArrayList<Option>?): LineSet(
    DataType.Text
){
    /**
     * 解析Text标签
     */
    override fun place(activity: Activity,data:MutableMap<String,Any>?): Parent<*, *> {
        val text = when(type){
            TextType.Text-> {
                val text= Text()
                text.viewBinding = Expand.getViewBinding(activity,text.getViewBindingCls())
                text
            }
            TextType.TextInput-> {
                val text= TextInput()
                text.viewBinding = Expand.getViewBinding(activity,text.getViewBindingCls())
                text
            }
            TextType.TextSelect-> {
                val text= TextSelect()
                text.viewBinding = Expand.getViewBinding(activity,text.getViewBindingCls())
                text
            }
            TextType.TextCheck-> {
                val text= TextCheck()
                text.viewBinding = Expand.getViewBinding(activity,text.getViewBindingCls())
                text
            }
        }
        text.line=this
        if (theme!=null){
            theme?.setTheme(text.getTargetView())
        }
        text.init(data)
        return text
    }
}

data class Option(val id:String,val title:String,val childOptions:ArrayList<Option>):Serializable
//count==-1 无限
data class ImgSet(var format:ImgFormat,val placeholder:CacheType,val placeholderName:String,val cache:CacheType,
                  val type:ImgType,val list:ListSet): LineSet(DataType.Img) {
    /**
     * 解析Img标签
     */
    override fun place(activity: Activity,data:MutableMap<String,Any>?): Parent<*, *> {
        val img = when (type) {
            ImgType.Icon -> {
                val img = Img()
                img.viewBinding = Expand.getViewBinding(activity,img.getViewBindingCls())
                img
            }
            ImgType.HideSingle -> {
                val img = ImgHideSingle()
                img.viewBinding = Expand.getViewBinding(activity,img.getViewBindingCls())
                img
            }
            ImgType.HideList -> {
                val img = ImgHideList()
                img.viewBinding = Expand.getViewBinding(activity,img.getViewBindingCls())
                img
            }
            ImgType.Bar -> {
                val img = ImgBar()
                img.viewBinding = Expand.getViewBinding(activity,img.getViewBindingCls())
                img
            }
        }
        img.line = this
        img.init(data)
        return img
    }
}

data class ListSet(val model:ListModel,val minCount:Int,val maxCount:Int)

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
    Script,
    //跳转
    Go,

}
enum class TextType{
    Text,
    TextInput,
    TextSelect,
    TextCheck,
}

enum class DataType{
    OnlyData,
    Group,
    Text,
    Img,
}
