package com.hq.general.model

import android.view.View
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hq.general.BaseActivity
import com.hq.general.expand.Expand
import com.hq.general.widget.form.Parent
import com.hq.tool.loge
import com.hq.tool.misc.Reflect
import java.io.Serializable

data class Theme(val padding:Padding?,val width:Int?,val height:Int?,val  orientation:Int?,
val back:String?,val icon:String?):Serializable{

    fun setTheme(parent: Parent<*,*>): Unit {
        if (padding!=null)
        parent.viewBinding.root.setPadding(padding.left,padding.top,padding.right,padding.down)

        if (orientation!=null&&parent.viewBinding.root is LinearLayout){
            (parent.viewBinding.root as LinearLayout).orientation=orientation
        }
        if (back!=null){
           val id= Reflect.getFieldValue("drawable",back, parent.viewBinding.root.context)
            parent.viewBinding.root.background=parent.viewBinding.root.context.resources.getDrawable(id)
        }
        when(parent.getTargetView()){
            is TextView->{

            }
            is ImageView->{

            }
            is AbsListView->{

            }
            is LinearLayout->{

            }
        }
    }
}
data class Padding(val left:Int,val right:Int,val top:Int,val down:Int):Serializable