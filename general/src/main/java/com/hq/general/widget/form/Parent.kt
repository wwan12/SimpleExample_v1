package com.hq.general.widget.form

import android.view.View
import androidx.viewbinding.ViewBinding
import com.hq.general.extraction.DataExtraction
import com.hq.general.model.LineSet

/**
 * 所有view行的父类
 */
abstract  class Parent<T : LineSet, E: ViewBinding> {

    lateinit var line:T

    lateinit var viewBinding:E

    lateinit var data: DataExtraction

    var onClick:((Parent<*,*>)->Unit)?=null

    abstract fun getTargetView(): View

    abstract fun getViewBindingCls():Class<E>

    abstract fun init()

    abstract fun check():Boolean

    abstract fun data():Any?

    abstract fun refresh()


    //abstract fun onClick(): Unit
}