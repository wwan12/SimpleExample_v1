package com.hq.generalsecurity.widget.group

import android.app.Activity
import android.text.InputFilter
import android.view.View
import com.hq.generalsecurity.databinding.LayerStandardGroupBinding
import com.hq.generalsecurity.databinding.LayerStandardTextBinding
import com.hq.generalsecurity.expand.DataType
import com.hq.generalsecurity.expand.GroupSet
import com.hq.generalsecurity.expand.TextSet
import com.hq.generalsecurity.widget.form.OnlyData
import com.hq.generalsecurity.widget.form.Parent
import com.hq.tool.toStringPro


class Group : Parent<GroupSet, LayerStandardGroupBinding>() {

    override fun getViewBindingCls(): Class<LayerStandardGroupBinding> {
        return LayerStandardGroupBinding::class.java
    }

    override fun init(data:MutableMap<String,Any>?) {
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

}