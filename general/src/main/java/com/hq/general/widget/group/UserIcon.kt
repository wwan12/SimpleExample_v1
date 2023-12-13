package com.hq.general.widget.group

import android.app.Activity
import android.view.View
import com.hq.general.databinding.LayerStandardGroupBinding
import com.hq.general.databinding.LayerStandardUserIconBinding
import com.hq.general.expand.StorageExpand
import com.hq.general.extraction.DataExtraction
import com.hq.general.model.GroupSet
import com.hq.general.widget.form.Parent
import com.hq.tool.toStringPro


class UserIcon : Parent<GroupSet, LayerStandardUserIconBinding>() {

    override fun getViewBindingCls(): Class<LayerStandardUserIconBinding> {
        return LayerStandardUserIconBinding::class.java
    }

    override fun init() {
        for (child in line.childs){
            when(child.title){
                "Name"->{
                    viewBinding.userName.text=StorageExpand.getLocalData<String>(child.serviceName).toStringPro()
                }
                "Supply"->{
                    viewBinding.userSupply.text=StorageExpand.getLocalData<String>(child.serviceName).toStringPro()
                }
            }
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