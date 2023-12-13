package com.hq.general.standardform


import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.hq.general.*
import com.hq.general.databinding.ActivityStandardFormBinding
import com.hq.general.expand.Expand
import com.hq.general.expand.Expand.getPage
import com.hq.general.expand.Expand.setFromAction
import com.hq.general.expand.Expand.toFormActivity
import com.hq.general.model.FormAction
import com.hq.general.model.FormStandardPage
import com.hq.general.model.PageActionType
import com.hq.general.widget.form.*
import com.hq.general.set.Flag
import com.hq.tool.misc.Reflect
import com.hq.tool.toStringPro

class StandardFormActivity:FormBaseActivity<ActivityStandardFormBinding,FormStandardPage>() {

    var lines= mutableListOf<Parent<*,*>>()


    override fun getBinding(): ActivityStandardFormBinding {
        return  ActivityStandardFormBinding.inflate(layoutInflater, baseBinding.container, true)
    }

    override fun initView() {
        super.initView()
        pageSet.getView(this,control){ bind,list->
            lines=list
            bind.root.layoutParams=LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
            viewBinding.standardLl.addView(bind.root)
            if (pageSet.action.pageBottom!=null){
                setFromAction( bind.standardActionButton,pageSet,lines,
                    pageSet.action.pageBottom!!,control
                )
            }
            if (pageSet.action.pageBottom2!=null){
                setFromAction( bind.standardActionButton2,pageSet,lines,
                    pageSet.action.pageBottom2!!,control
                )
            }

            for (line in lines){
                line.onClick=onClick
            }
        }
    }


}