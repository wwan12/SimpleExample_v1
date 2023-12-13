package com.hq.general.standardform

import com.hq.general.BaseActivity
import com.hq.general.databinding.ActivityStandardFreeBinding
import com.hq.general.model.FreeStandardPage

class StandardFreeActivity: FormBaseActivity<ActivityStandardFreeBinding,FreeStandardPage>() {
    override fun getBinding(): ActivityStandardFreeBinding {
        return  ActivityStandardFreeBinding.inflate(layoutInflater, baseBinding.container, true)
    }

    override fun initView() {

    }
}