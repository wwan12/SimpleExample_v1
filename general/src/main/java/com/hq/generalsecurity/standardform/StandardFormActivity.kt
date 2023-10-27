package com.hq.generalsecurity.standardform


import android.view.View

import com.hq.generalsecurity.*
import com.hq.generalsecurity.databinding.ActivityStandardFormBinding
import com.hq.generalsecurity.expand.*
import com.hq.generalsecurity.expand.Expand.http
import com.hq.generalsecurity.widget.form.*
import com.hq.generalsecurity.set.Flag

import com.hq.tool.onLoad
import com.hq.tool.toStringPro
import com.hq.tool.toast

class StandardFormActivity:BaseActivity<ActivityStandardFormBinding>() {

    var lines= mutableListOf<Parent<*,*>>()

    lateinit var pageSet: FormStandardPage

    override fun getBinding(): ActivityStandardFormBinding {
        return  ActivityStandardFormBinding.inflate(layoutInflater, baseBinding.container, true)
    }

    override fun initView() {
        pageSet=intent.getSerializableExtra(Flag.PAGE_FLAG) as FormStandardPage

        setTitle(pageSet.pageName)


        pageSet.getView(this){ bind,list->
            lines=list
            viewBinding.standardLl.addView(bind.root)
            bind.standardActionButton.setOnClickListener {
                pageSet.check(this,lines){
                    pageSet.collectData(this,lines){
                        if (it){
                            finish()
                        }
                    }
                }
            }
        }
    }


}