package com.hq.general.expand

import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.viewbinding.ViewBinding
import com.hq.general.model.LineSet

abstract class ItemControl<E:ViewBinding> {

    abstract fun getView(adapterView: AdapterView<*>, data:MutableMap<String,Any>, lineSets:ArrayList<LineSet>, viewBinding: E)

    abstract fun getViewBindingCls(): Class<E>

}