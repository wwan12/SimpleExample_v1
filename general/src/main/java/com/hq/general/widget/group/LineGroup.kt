package com.hq.general.widget.group

import android.app.Activity
import android.view.View
import com.hq.general.databinding.LayerStandardGroupBinding
import com.hq.general.extraction.DataExtraction
import com.hq.general.model.GroupSet
import com.hq.general.widget.form.Parent


class LineGroup : Parent<GroupSet, LayerStandardGroupBinding>() {

    override fun getViewBindingCls(): Class<LayerStandardGroupBinding> {
        return LayerStandardGroupBinding::class.java
    }

    override fun init() {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        viewBinding.ll.setOnClickListener {
            viewBinding.groupLl.visibility=  if( viewBinding.groupLl.visibility==View.GONE){
                View.VISIBLE}else{View.GONE}
        }
        for (child in line.childs){
           val c= child.place(viewBinding.textTitle.context as Activity,data)
            viewBinding.groupLl.addView(c.viewBinding.root)
        }
    }

    override fun data(): Any {
       return ""
    }

    override fun check(): Boolean {
        return true
    }

    override fun getTargetView(): View {
        return viewBinding.root
    }

    override fun refresh() {

    }

}