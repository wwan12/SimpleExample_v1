package com.hq.general.standardform

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.hq.general.BaseActivity
import com.hq.general.BaseFragment
import com.hq.general.control.ControlBase
import com.hq.general.model.PageSet
import com.hq.general.set.Flag
import com.hq.general.widget.form.Parent

abstract class FormBaseFragment<T : ViewBinding,E:PageSet>(val pageSet:E):BaseFragment<T>() {

    lateinit var activity: Activity

    var control:ControlBase<*>?=null

    val onClick:(Parent<*, *>)->Unit={
        control?.onLayoutClick(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity= requireActivity()

        if (pageSet.control!=null){
            control= pageSet.control!!.getControl(activity,pageSet)
        }

        control?.onCreate()

    }


//    override fun onPause() {
//        super.onPause()
//        control?.onPause()
//    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden){
            control?.onPause()
        }else{
            control?.onRestart()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        control?.onDestroy()
    }
}