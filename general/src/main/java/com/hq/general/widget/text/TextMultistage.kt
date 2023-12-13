package com.hq.general.widget.text

import android.app.Activity
import android.text.InputFilter
import android.view.Gravity
import android.view.View
import android.widget.*
import com.hq.general.R
import com.hq.general.databinding.ItemMultistageBinding
import com.hq.general.databinding.LayerStandardTextBinding
import com.hq.general.expand.StorageExpand
import com.hq.general.extraction.DataExtraction
import com.hq.general.model.Option
import com.hq.general.model.TextSet
import com.hq.general.widget.form.Parent
import com.hq.tool.toStringPro
import com.hq.tool.widget.adapter.SimpleBindAdapter
import com.hq.tool.widget.openAnyViewWindowCenter

/**
 * 多级选择框
 * TODO
 */
class TextMultistage : Parent<TextSet, LayerStandardTextBinding>() {

    private lateinit var multistageAdapter :SimpleBindAdapter<Option,ItemMultistageBinding>
//
    private var pop: PopupWindow?=null

    private val selectOptions= arrayListOf<Option>()

    private val showList= arrayListOf<Option>()


    private val sourceData= arrayListOf<Option>()


    override fun getViewBindingCls(): Class<LayerStandardTextBinding> {
        return LayerStandardTextBinding::class.java
    }

    override fun init() {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        viewBinding.textContent.hint=  line.hint
        if (line.serviceName.isNotEmpty()) {
           if (line.options!=null){
               sourceData.addAll(line.options!!)
           }else{
               line.options= StorageExpand.getLocalData<ArrayList<Option>>(line.serviceName)
               if (line.options!=null){
                   sourceData.addAll(line.options!!)
               }
           }
            line.data = data.data.toStringPro()
            viewBinding.textContent.text=getSelectName(line.data)
        }

        if (line.maxLength>0){
            viewBinding.textContent.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(line.maxLength))
        }
        viewBinding.textContent.setOnClickListener {
            openManagerPop(viewBinding.root.context as Activity)
        }
        multistageAdapter= SimpleBindAdapter(viewBinding.root.context as Activity,ItemMultistageBinding::class.java, showList)
        {option, itemMultistageBinding ->
            itemMultistageBinding.optionName.text=option.title
        }
    }

    override fun data(): Any {
       return viewBinding.textContent.text.toString()
    }

    override fun check(): Boolean {
        return true
    }

    override fun getTargetView(): View {
        return viewBinding.textContent
    }

    private fun select(option: Option){
        selectOptions.add(option)
        if (option.childOptions==null){
            line.data=option.id
            pop?.dismiss()
            viewBinding.textContent.text=  getSelectName()
        }else{
            showList.clear()
            showList.addAll(option.childOptions)
            multistageAdapter.notifyDataSetChanged()
        }
    }


    fun openManagerPop(activity: Activity): Unit {
        pop = activity.openAnyViewWindowCenter(
            R.layout.pop_text_multistage, LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM)
        pop?.contentView?.findViewById<ImageView>(R.id.close)?.setOnClickListener { pop?.dismiss() }
        pop?.contentView?.findViewById<TextView>(R.id.select_name)?.text= getSelectName()

        val listview= pop?.contentView?.findViewById<ListView>(R.id.list)
        showList.clear()
        showList.addAll(sourceData)
        listview?.adapter= multistageAdapter
//        multistageAdapter.notifyDataSetChanged()
        listview?.setOnItemClickListener { parent, view, position, id ->
            select(showList[position])
        }

        selectOptions.clear()
    }

    fun getSelectName(id:String): String? {
        for (d in sourceData) {
            if (d.id == id) {
                return d.title
            }
            if (d.childOptions!=null){
               val cs= getSelectName(id)
                if (cs!=null){
                    return cs
                }
            }
        }
        return null
    }

    fun getSelectName(): String {
        var ss=""
        for (o in selectOptions){
            ss+="${o.title} "
        }
        return  ss
    }
    override fun refresh() {

    }

}