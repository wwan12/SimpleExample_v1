package com.hq.general.widget.form

import android.view.View
import com.hq.general.databinding.LayerStandardNullBinding
import com.hq.general.databinding.LayerStandardTextBinding
import com.hq.general.extraction.DataExtraction
import com.hq.general.model.LineSet
import com.hq.tool.toStringPro

class OnlyData:Parent<LineSet, LayerStandardNullBinding>()  {


    override fun getViewBindingCls(): Class<LayerStandardNullBinding> {
        return LayerStandardNullBinding::class.java
    }
    override fun init() {
        if (line.serviceName.isNotEmpty()) {
            line.data = data.data.toStringPro()
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

    override fun refresh() {

    }


}