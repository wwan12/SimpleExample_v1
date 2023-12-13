package com.hq.general.control

import android.app.Activity
import androidx.viewbinding.ViewBinding
import com.hq.general.model.FormAction
import com.hq.general.model.PageSet
import com.hq.general.standardform.FormBaseActivity
import com.hq.general.widget.form.Parent

/**
 * UI 控制器
 */
abstract class ControlBase<E:PageSet>(val activity: Activity,val pageSet: E) {
    abstract fun onCreate()
    abstract fun onPause()
    abstract fun onRestart()
    abstract fun onDestroy()

    abstract fun onLayoutCreate(layout:Parent<*,*>)

    abstract fun onLayoutClick(layout:Parent<*,*>)

    abstract fun onActionClick(action: FormAction,call:()->Unit)
}