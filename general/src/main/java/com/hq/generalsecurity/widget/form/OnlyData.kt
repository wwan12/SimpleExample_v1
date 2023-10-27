package com.hq.generalsecurity.widget.form

import android.view.View
import com.hq.generalsecurity.databinding.LayerStandardTextBinding
import com.hq.generalsecurity.expand.LineSet
import com.hq.tool.toStringPro

class OnlyData:Parent<LineSet, LayerStandardTextBinding>()  {


    override fun getViewBindingCls(): Class<LayerStandardTextBinding> {
        return LayerStandardTextBinding::class.java
    }
    override fun init(data: MutableMap<String, Any>?) {
        if (data!=null&&line.serviceName.isNotEmpty()) {
            line.data = data[line.serviceName].toStringPro()
        }
    }


    override fun check(): Boolean {
        return true
    }

    override fun data(): Any {
       return  line.data
    }

    override fun getTargetView(): View {
        return viewBinding.root
    }


}