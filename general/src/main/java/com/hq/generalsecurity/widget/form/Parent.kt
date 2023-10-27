package com.hq.generalsecurity.widget.form

import android.view.View
import androidx.viewbinding.ViewBinding
import com.hq.generalsecurity.expand.LineSet

abstract  class Parent<T :LineSet, E: ViewBinding> {

    lateinit var line:T

    lateinit var viewBinding:E


//    inline fun <reified E: ViewBinding> getViewBinding(activity: Activity,parent: ViewGroup): E {
//        val inflate =   E::class.java.getDeclaredMethod(
//            "inflate",
//            LayoutInflater::class.java,
//            ViewGroup::class.java,
//            Boolean::class.javaPrimitiveType
//        )
//        val viewBinding = inflate.invoke(null, activity.layoutInflater, parent, false) as E
//        return viewBinding
//    }
    abstract fun getTargetView(): View

    abstract fun getViewBindingCls():Class<E>

    abstract fun init(data:MutableMap<String,Any>?)

    abstract fun check():Boolean

    abstract fun data():Any?

}