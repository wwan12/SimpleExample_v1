package com.hq.generalsecurity.expand

import android.view.View
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hq.generalsecurity.BaseActivity

class Theme{

    val target:Target=Target.Self

    var active=true

    val sets= arrayListOf<ThemeItem>()

    fun setTheme(view:View): Unit {
        if (!active||sets.size==0){
            return
        }
        when(view){
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
    
    enum class Type(val value:String) {
        Layout("layout"),
        Drawable("drawable"),
        Mipmap("mipmap"),
        Color("color"),
    }

    enum class Target{
        Self,
        ListItem,
        Id
    }
}

data class ThemeItem(val type: Theme.Type, val name:String){
    
    fun getId(): Int {
        return Expand.getFieldValue(type.value,name,BaseActivity.active)
    }
}