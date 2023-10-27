package com.hq.generalsecurity.widget.text

import android.text.InputFilter
import android.view.View
import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider
import com.hq.generalsecurity.databinding.LayerStandardTextBinding
import com.hq.generalsecurity.expand.Expand
import com.hq.generalsecurity.expand.TextSet
import com.hq.generalsecurity.widget.form.Parent
import com.hq.tool.toStringPro

/**
 * 多级选择框
 * TODO
 */
class TextMultistage : Parent<TextSet, LayerStandardTextBinding>() {

    //    private var gxAdapter :GxAdapter?=null
//
//    private var pop: PopupWindow?=null

    private  val hierarchy= Hierarchy()


    override fun getViewBindingCls(): Class<LayerStandardTextBinding> {
        return LayerStandardTextBinding::class.java
    }

    override fun init(data:MutableMap<String,Any>?) {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        viewBinding.textContent.hint=  line.hint
        if (data!=null&&line.serviceName.isNotEmpty()) {
            line.data = data[line.serviceName].toStringPro()
        }
        viewBinding.textContent.text=  line.data
        if (line.maxLength>0){
            viewBinding.textContent.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(line.maxLength))
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
//    private fun gx(): Unit {
//        //目前三级菜单等待之后去掉
//        val gxView=  standard_ll.get(standard_ll.childCount-1).findViewById<TextView>(R.id.text_content)
//        Expand.initManager(this,{
//            gxView.tag=gxView.text.toString()
//            gxView.text= Expand.getManagerName(gxView.text.toString())
//        },{id,text->
//            gxView.text=text
//            gxView.tag=id
//        })
//        standard_ll.get(standard_ll.childCount-1).findViewById<TextView>(R.id.text_content).setOnClickListener {
//            Expand.openManagerPop(this)
//        }
//    }

    //    fun initManager(activity: Activity,loadCall:()->Unit,selectCall:(String,String)->Unit): Unit {
//        Api.getManager({
//            gxAdapter = GxAdapter(activity as AppCompatActivity) { data ->
//                if (data.children == null || data.children!!.size == 0) {
//                    pop?.dismiss()
//                    pop = null
//                    selectCall(data.id, data.label)
//                }
//            }
//            gxAdapter?.init(it)
//            for (f in it.data) {
//                if (f.children != null) {
//                    for (f1 in f.children!!) {
//                        hierarchy.firstList.add(f1.label)
//                        hierarchy.thirdIds[f1.label] = f1.id
//                        getHierarchyChildren(f1, hierarchy.secondList) { t ->
//                            getHierarchyChildren(t, hierarchy.thirdList) {
//                            }
//                        }
//                    }
//                }
//            }
//            loadCall()
//
//        }, {
//
//        })
//    }

    //    fun openManagerPop(activity: Activity): Unit {
//        pop = activity.openAnyViewWindowCenter(
//            R.layout.pop_list_gx, LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM)
//        pop?.contentView?.gx_close?.setOnClickListener { pop?.dismiss() }
//        pop?.contentView?.gx_list?.adapter= gxAdapter
//        gxAdapter?.open()
//    }
    fun getManagerName(sysOrgCode:String): String {
        for (th in hierarchy.thirdIds) {
            if (th.value == sysOrgCode) {
                return th.key
            }
        }
        return ""
    }
//
//    private fun getHierarchyChildren(gxData: GXData, map: MutableMap<String, MutableList<String>>, children:(GXData)->Unit): Unit {
//        val tlist=mutableListOf<String>()
//        for (t in gxData.children!!){
//            tlist.add(t.label)
//            if (t.children!=null){
//                children(t)
//            }
//            hierarchy.thirdIds[t.label]=t.id
//        }
//        map[gxData.label]=tlist
//    }


    private class Hierarchy(): LinkageProvider {

        val firstList= mutableListOf<String>()

        val secondList= mutableMapOf<String,MutableList<String>>()
        val thirdList=mutableMapOf<String,MutableList<String>>()

        val thirdIds= mutableMapOf<String,String>()

        override fun firstLevelVisible(): Boolean {
            return true
        }

        override fun thirdLevelVisible(): Boolean {
            return true
        }

        override fun provideFirstData(): MutableList<*> {
            return firstList
        }

        override fun linkageSecondData(firstIndex: Int): MutableList<*> {
            return secondList[firstList[firstIndex]]!!
        }

        override fun linkageThirdData(firstIndex: Int, secondIndex: Int): MutableList<*> {
            val s= secondList[firstList[firstIndex]]!![secondIndex]
            return thirdList[s]!!
        }

        override fun findFirstIndex(firstValue: Any?): Int {
            return firstList.indexOf(firstValue.toString())
        }

        override fun findSecondIndex(firstIndex: Int, secondValue: Any?): Int {
            return secondList[firstList[firstIndex]]!!.indexOf(secondValue)
        }

        override fun findThirdIndex(firstIndex: Int, secondIndex: Int, thirdValue: Any?): Int {
            val s= secondList[firstList[firstIndex]]!![secondIndex]
            return thirdList[s]!!.indexOf(thirdValue)
        }
    }


}