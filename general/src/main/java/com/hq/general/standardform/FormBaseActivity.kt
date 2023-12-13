package com.hq.general.standardform

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.PopupWindow
import androidx.viewbinding.ViewBinding
import com.hq.general.BaseActivity
import com.hq.general.control.ControlBase
import com.hq.general.expand.Expand
import com.hq.general.model.FormAction
import com.hq.general.model.FormStandardPage
import com.hq.general.model.PageActionType
import com.hq.general.model.PageSet
import com.hq.general.set.Flag
import com.hq.general.widget.form.Parent
import com.hq.tool.loge
import com.hq.tool.misc.Reflect

abstract class FormBaseActivity<T : ViewBinding,E:PageSet>:BaseActivity<T>() {

    lateinit var pageSet: E

    var control:ControlBase<*>?=null

    val onClick:(Parent<*, *>)->Unit={
        "点击:${it.line.title}".loge()
        control?.onLayoutClick(it)
    }

    override fun initView() {
        pageSet=intent.getSerializableExtra(Flag.PAGE_FLAG) as E

        setTitle(pageSet.pageName)

        if (pageSet.control!=null){
            control= pageSet.control!!.getControl(this,pageSet)
        }
        control?.onCreate()

    }


    override fun onPause() {
        super.onPause()
        control?.onPause()
    }

    override fun onRestart() {
        super.onRestart()
        control?.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
        control?.onDestroy()
    }

}