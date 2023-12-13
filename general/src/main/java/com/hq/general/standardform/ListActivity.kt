package com.hq.general.standardform

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.BaseAdapter
import androidx.core.widget.addTextChangedListener
import androidx.viewbinding.ViewBinding
import com.hq.general.BaseActivity
import com.hq.general.databinding.ActivityListBinding
import com.hq.general.databinding.ItemListBinding
import com.hq.general.expand.*
import com.hq.general.model.CacheType
import com.hq.general.model.ClickAction
import com.hq.general.model.ListStandardPage
import com.hq.general.widget.form.ParentLayout
import com.hq.general.set.Flag
import com.hq.tool.loge
import com.hq.tool.misc.Reflect
import com.hq.tool.toStringPro
import com.hq.tool.toast
import com.hq.tool.widget.adapter.SimpleBindAdapter
import java.util.*
import kotlin.collections.ArrayDeque

/**
 * 单列表页面
 */
class ListActivity: FormBaseActivity<ActivityListBinding,ListStandardPage>() {

    var page=1
    var pageSize=10

//    val layouts= mutableListOf<ParentLayout>()
    val showList= arrayListOf<MutableMap<String,Any>>()

//    val loadStack=Stack<String>()
    var cacheWordKey=""

    var isload=false

    override fun getBinding(): ActivityListBinding {
       return ActivityListBinding.inflate(layoutInflater,baseBinding.container,true)
    }

    override fun initView() {
        super.initView()
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
        if (pageSet.itemLayoutName!=null) {
            val control = Reflect.getEntity<ItemControl<ViewBinding>>(pageSet.itemLayoutName!!)
            viewBinding.list.adapter =
                SimpleBindAdapter(this, control.getViewBindingCls(), showList)
                { t, e ->
                    control.getView(viewBinding.list,t, pageSet.lineSets, e)
                }
        }else{
            viewBinding.list.adapter=SimpleBindAdapter(this,ItemListBinding::class.java,showList)
            { t, e ->
                for (line in pageSet.lineSets){
                    val de= Global.getDataExtraction(pageSet.load!!.loadUrl,
                        line.source ?: CacheType.None,line.serviceName,line.dataType)
                    de.data=t[line.serviceName]
                    val p = line.place(this,de)
                    when(line.position){
                        Flag.Dir.Left->   e.left.addView(p.viewBinding.root)
                        Flag.Dir.Right->  e.right.addView(p.viewBinding.root)
                        Flag.Dir.Top->  e.top.addView(p.viewBinding.root)
                        Flag.Dir.Down->  e.down.addView(p.viewBinding.root)
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
                    ClickAction.Go->{
                        if (pageSet.detailsPost==true){
                            showList[position]["code"]="200"
                            StorageExpand.addLocalData(Flag.LIST_ITEM,showList[position])
                            showList[position].toString().loge()

                            Expand.toFormActivity(this,pageSet.route.toStringPro())

                        }
                    }
                }
            }
        }

        load("",false)

    }

    fun load(keyWord:String,clear:Boolean): Unit {
        pageSet.load(keyWord,page,pageSize,{
            if (clear){
                showList.clear()
            }
            "suss".loge()
            isload=false
            showList.addAll(it)
            (viewBinding.list.adapter as BaseAdapter).notifyDataSetChanged()
        },{
            "fail".loge()
            isload=false
//            it.failMsg.toast(this)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK){
            viewBinding.listRefreshLayout.autoRefresh()
        }
    }
}