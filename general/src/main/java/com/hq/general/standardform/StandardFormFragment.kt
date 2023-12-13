package com.hq.general.standardform


import android.view.ViewGroup
import android.widget.LinearLayout
import com.hq.general.*
import com.hq.general.databinding.ActivityStandardFormBinding
import com.hq.general.expand.*
import com.hq.general.expand.Expand.getPage
import com.hq.general.expand.Expand.setFromAction
import com.hq.general.model.FormAction
import com.hq.general.model.FormStandardPage
import com.hq.general.model.PageActionType
import com.hq.general.widget.form.*
import com.hq.tool.toStringPro

class StandardFormFragment(pageSet: FormStandardPage): FormBaseFragment<ActivityStandardFormBinding,FormStandardPage>(pageSet) {

    var lines: MutableList<Parent<*,*>>?=null


    override fun initView() {
        pageSet.getView(requireActivity(),control){ bind,list->
            lines=list
            bind.root.layoutParams=
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            viewBinding.standardLl.addView(bind.root)
            if (pageSet.action.pageBottom!=null){
                activity.setFromAction( bind.standardActionButton,pageSet,lines!!,
                    pageSet.action.pageBottom!!,control
                )
            }
            if (pageSet.action.pageBottom2!=null){
                activity.setFromAction( bind.standardActionButton2,pageSet,lines!!,
                    pageSet.action.pageBottom2!!,control
                )
            }

            for (line in lines!!){
                line.onClick=onClick
            }
        }

    }

    fun A(): Unit {

    }


    override fun <E> setData(e: E) {

    }


}