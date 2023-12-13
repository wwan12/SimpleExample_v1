package com.hq.general.widget.text

import android.app.Activity
import android.text.InputFilter
import android.view.View
import com.hq.general.databinding.LayerStandardTextBinding
import com.hq.general.expand.Expand
import com.hq.general.expand.StorageExpand
import com.hq.general.extraction.DataExtraction
import com.hq.general.model.ClickAction
import com.hq.general.model.TextSet
import com.hq.general.widget.form.Parent
import com.hq.tool.misc.Reflect
import com.hq.tool.toStringPro
import com.hq.tool.widget.openDateSelect
import com.hq.tool.widget.openTimeSelect


class Text : Parent<TextSet, LayerStandardTextBinding>() {

    override fun getViewBindingCls(): Class<LayerStandardTextBinding> {
        return LayerStandardTextBinding::class.java
    }

    override fun init() {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        viewBinding.textContent.hint=  line.hint
        if (line.serviceName.isNotEmpty()) {
            line.data = data.data.toStringPro()
        }
        viewBinding.textContent.text=  line.data
        if (line.maxLength>0){
            viewBinding.textContent.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(line.maxLength))
        }

        if (line.onClick!=null){
            viewBinding.textContent.setOnClickListener {
                when(line.onClick){
                    ClickAction.Go->{
                        if (line.route?.contains("Activity")==true){
                            Reflect.startActivity(viewBinding.root.context as Activity,line.route!!)
                        }else{
                            Expand.toFormActivity(viewBinding.root.context as Activity, line.route!!)
                        }
                    }
                    ClickAction.Time->{
                        viewBinding.textContent.openTimeSelect()
                    }
                    ClickAction.Date-> viewBinding.textContent.openDateSelect()
                    ClickAction.DateTime->{
                        var t=""
                        viewBinding.textContent.openDateSelect{
                            t=it
                            viewBinding.textContent.openTimeSelect {
                                viewBinding.textContent.text ="${t} ${it}"
                            }
                        }
                    }
                    else->{

                    }
                }
                onClick?.invoke(this)
            }
        }





    }

    override fun data(): Any {
       return viewBinding.textContent.text.toString()
    }

    override fun check(): Boolean {
      return  when(line.onClick){
            ClickAction.Time, ClickAction.Date,ClickAction.DateTime->{
                viewBinding.textContent.text.toString().isNotEmpty()
            }
            else->{ true }
        }

    }

    override fun getTargetView(): View {
        return viewBinding.textContent
    }
    override fun refresh() {
        viewBinding.textContent.text=  data.data.toStringPro()
    }
}