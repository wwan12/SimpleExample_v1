package com.hq.general.standardform

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.BaseAdapter
import androidx.core.widget.addTextChangedListener
import androidx.viewbinding.ViewBinding
import com.google.gson.Gson
import com.hq.general.BaseFragment
import com.hq.general.databinding.ActivityListBinding
import com.hq.general.databinding.FragmentListBinding
import com.hq.general.databinding.ItemListBinding
import com.hq.general.model.ClickAction
import com.hq.general.expand.Expand
import com.hq.general.expand.Global
import com.hq.general.expand.ItemControl
import com.hq.general.expand.StorageExpand
import com.hq.general.model.CacheType
import com.hq.general.model.ListStandardPage
import com.hq.general.set.Flag
import com.hq.tool.loge
import com.hq.tool.misc.Reflect
import com.hq.tool.toStringPro
import com.hq.tool.toast
import com.hq.tool.widget.adapter.SimpleBindAdapter

class ListFragment<T,E: ViewBinding>( pageSet: ListStandardPage, val cls:(Class<E>?)=null, val list: (List<T>?)=null, val viewCall:((T, E)->Unit)?=null):
    FormBaseFragment<ActivityListBinding,ListStandardPage>(pageSet) {

    var page=0
    var pageSize=10

    val showList= arrayListOf<MutableMap<String,Any>>()
    var cacheWordKey=""
    var isload=false
    override fun initView() {

        viewBinding.listRefreshLayout.setOnRefreshListener {
            page=1
            isload=true
            it.finishRefresh(1000)
            load(cacheWordKey,true)
        }
        viewBinding.listRefreshLayout.setOnLoadMoreListener {
            page++
            isload=true
            it.finishLoadMore(1000)

            load(cacheWordKey, pageSet.paging != true)
        }

        if (pageSet.launchSearch==true){
            viewBinding.barSearch.root.visibility= View.VISIBLE
            viewBinding.barSearch.searchEt.addTextChangedListener {
//                if (it.toString()!=cacheWordKey){
//                    loadStack.add(it.toStringPro())
//                }
                cacheWordKey=it.toStringPro()
                load(cacheWordKey,true)
            }
        }

        if (cls!=null&&list!=null&&viewCall!=null){
            viewBinding.list.adapter=SimpleBindAdapter(requireActivity(),cls,list, viewCall)
        }else{
             if (pageSet.itemLayoutName!=null) {
                val control= Reflect.getEntity<ItemControl<ViewBinding>>(pageSet.itemLayoutName!!)
                 viewBinding.list.adapter=SimpleBindAdapter(requireActivity(), control.getViewBindingCls(),showList)
                 { t, e ->
                     control.getView(t,pageSet.lineSets, e)
                 }
            }else{
                 viewBinding.list.adapter=SimpleBindAdapter(requireActivity(), ItemListBinding::class.java,showList)
                 { t, e ->
                     for (line in pageSet.lineSets){
                         val de= Global.getDataExtraction(pageSet.load!!.loadUrl,line.source ?: CacheType.None,line.serviceName,line.dataType)
                         de.data=t[line.serviceName]
                         val p = line.place(requireActivity(),de)
                         when(line.position){
                             Flag.Dir.Left->   e.left.addView(p.viewBinding.root)
                             Flag.Dir.Right->  e.right.addView(p.viewBinding.root)
                             Flag.Dir.Top->  e.top.addView(p.viewBinding.root)
                             Flag.Dir.Down->  e.down.addView(p.viewBinding.root)
                         }
                     }
                 }
            }


        }
        if (pageSet.onItemClick!=null){
            viewBinding.list.setOnItemClickListener { parent, view, position, id ->
                if (isload){
                    return@setOnItemClickListener
                }
                when(pageSet.onItemClick) {
                    ClickAction.Go-> {
                        if (pageSet.detailsPost==true){
                            showList[position]["code"]="200"
                            StorageExpand.addLocalData(Flag.LIST_ITEM,showList[position])

                            Expand.toFormActivity(requireActivity(),pageSet.route.toStringPro())

                        }
                    }
                }
            }
        }
        load("",false)
    }


    override fun <E> setData(e: E) {

    }

    fun load(keyWord:String,clear:Boolean): Unit {
        "请求:${pageSet.pageName}-${pageSet.load!!.loadUrl}".loge()

        pageSet.load(keyWord,page,pageSize,{
            if (clear){
                showList.clear()
            }
            showList.addAll(it)
            (viewBinding.list.adapter as BaseAdapter).notifyDataSetChanged()
            isload=false
        },{
            isload=false
            it.failMsg.toast(requireActivity())
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK){
            viewBinding.listRefreshLayout.autoRefresh()
        }
    }
}