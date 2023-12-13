package com.hq.general.widget.form

import android.view.View
import android.widget.LinearLayout
import com.hq.general.BaseActivity
import com.hq.general.expand.Global
import com.hq.general.model.DataType
import com.hq.general.model.LineSet
import java.util.ArrayList

class ParentLayout(val activity:BaseActivity<*>) {

//    val lines= mutableListOf<Parent<*,*>>()


//    fun show(layout:LinearLayout, lineSets: ArrayList<LineSet>, data:MutableMap<String,Any>): Unit {
//        for (line in lineSets ){
//            var view: View?=null
//            val de= Global.getDataExtraction("",line.serviceName,line.dataType)
//            de.data=data[line.serviceName]
//            val ts = line.place(activity,de)
//            lines.add(ts)
//            when(line.dataType){
//                DataType.OnlyData->{
//
//                }
//                else->{
//                    view=  ts.viewBinding.root
//                }
//            }
//            if (view!=null){
//                if (line.serviceName.isNotEmpty()){
//                    view.tag="${line.dataType}:${line.servicePath}:${line.serviceName}"
//                }
//                layout.addView(view)
//            }
//
//        }
//    }
//
//    fun setData(data:MutableMap<String,Any>): Unit {
//
//    }
}