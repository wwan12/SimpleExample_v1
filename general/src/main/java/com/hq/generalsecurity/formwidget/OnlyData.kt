package com.hq.generalsecurity.formwidget

import com.hq.generalsecurity.databinding.LayerStandardTextBinding
import com.hq.generalsecurity.expand.LineSet

class OnlyData:Parent<LineSet, LayerStandardTextBinding>()  {


    override fun getViewBindingCls(): Class<LayerStandardTextBinding> {
        return LayerStandardTextBinding::class.java
    }

    override fun init() {
    }

    override fun check(): Boolean {
        return true
    }

    override fun data(): Any? {
       return  line.data
    }
}