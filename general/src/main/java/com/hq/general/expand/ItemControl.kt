package com.hq.general.expand

import androidx.viewbinding.ViewBinding
import com.hq.general.model.LineSet

abstract class ItemControl<E:ViewBinding> {

    abstract fun getView(data:MutableMap<String,Any>, lineSets:ArrayList<LineSet>, viewBinding: E)

    abstract fun getViewBindingCls(): Class<E>

}