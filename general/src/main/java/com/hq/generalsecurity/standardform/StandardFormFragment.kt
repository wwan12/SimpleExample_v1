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

class StandardFormFragment(val pageSet: FormStandardPage):BaseFragment<ActivityStandardFormBinding>() {

    var lines: MutableList<Parent<*,*>>?=null

    override fun initView() {
        pageSet.getView(requireActivity()){ bind,list->
            lines=list
            viewBinding.standardLl.addView(bind.root)
            bind.standardActionButton.setOnClickListener {
                pageSet.check(requireActivity(),lines!!){
                    pageSet.collectData(requireActivity(),lines!!){
                        if (it){
                            requireActivity().finish()
                        }
                    }
                }
            }
        }

    }


    override fun <E> setData(e: E) {

    }


}