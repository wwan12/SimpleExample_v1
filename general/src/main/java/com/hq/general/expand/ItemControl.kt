package com.hq.general.expand

import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.viewbinding.ViewBinding
import com.hq.general.model.LineSet
import com.scwang.smartrefresh.layout.SmartRefreshLayout

abstract class ItemControl<E:ViewBinding> (){

    var refresh: SmartRefreshLayout?=null

    var adapterView: AdapterView<*>?=null

    abstract fun getView( data:MutableMap<String,Any>, lineSets:ArrayList<LineSet>, viewBinding: E)

    abstract fun getViewBindingCls(): Class<E>

}