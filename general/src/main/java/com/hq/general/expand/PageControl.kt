package com.hq.general.expand

import androidx.viewbinding.ViewBinding
import com.hq.general.model.LineSet
import com.hq.general.model.PageSet
import com.hq.tool.http.SuccessData

abstract class PageControl<T:PageSet,E: ViewBinding>  {
    abstract fun initView(data:SuccessData, pageSet:T, viewBinding: E)

    abstract fun onRestart()

    abstract fun onDestroy()
}