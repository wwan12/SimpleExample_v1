package com.hq.generalsecurity.formwidget

import android.view.View
import android.widget.LinearLayout
import com.hq.generalsecurity.BaseActivity
import com.hq.generalsecurity.expand.DataType
import com.hq.generalsecurity.expand.ImgSet
import com.hq.generalsecurity.expand.LineSet
import com.hq.generalsecurity.expand.TextSet
import com.hq.tool.toStringPro
import java.util.ArrayList

class ParentLayout(val activity: BaseActivity<*>) {

    val lines= mutableListOf<Parent<*,*>>()


    fun show(layout:LinearLayout,lineSets: ArrayList<LineSet>, data:MutableMap<String,Any>): Unit {
        for (line in lineSets ){
            if (line.serviceName.isNotEmpty()){
                if (data.containsKey(line.serviceName)) {
                    line.data = data[line.serviceName].toStringPro()
                }
            }
            var view: View?=null
            when(line.dataType){
                DataType.OnlyData->{
                    val od= OnlyData()
                    od.line=line
                }
                DataType.Text->{
                    val ts=activity. placeText(line as TextSet)
                    lines.add(ts)
                    view=  ts.viewBinding.root
                }
                DataType.Img->{
                    val img=activity.placeImg(line as ImgSet)
                    lines.add(img)
                    view=  img.viewBinding.root
                }
            }
            if (view!=null){
                if (line.serviceName.isNotEmpty()){
                    view.tag="${line.dataType}:${line.servicePath}:${line.serviceName}"
                }
                layout.addView(view)
            }

        }
    }

    fun setData(data:MutableMap<String,Any>): Unit {

    }
}