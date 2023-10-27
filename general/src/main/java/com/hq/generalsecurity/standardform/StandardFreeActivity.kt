package com.hq.generalsecurity.standardform

import com.hq.generalsecurity.BaseActivity
import com.hq.generalsecurity.databinding.ActivityStandardFreeBinding

class StandardFreeActivity:BaseActivity<ActivityStandardFreeBinding>() {
    override fun getBinding(): ActivityStandardFreeBinding {
        return  ActivityStandardFreeBinding.inflate(layoutInflater, baseBinding.container, true)
    }

    override fun initView() {

    }
}