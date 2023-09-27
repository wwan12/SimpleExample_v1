package com.hq.generalsecurity.standardform

import android.view.View
import android.widget.BaseAdapter
import androidx.viewbinding.ViewBinding
import com.hq.generalsecurity.BaseActivity
import com.hq.generalsecurity.databinding.ActivityListBinding
import com.hq.generalsecurity.databinding.ItemListBinding
import com.hq.generalsecurity.expand.*
import com.hq.generalsecurity.formwidget.Parent
import com.hq.generalsecurity.formwidget.ParentLayout
import com.hq.tool.http.Http
import com.hq.tool.toast
import com.hq.tool.widget.adapter.SimpleBindAdapter
import okhttp3.Headers

class ListActivity: BaseActivity<ActivityListBinding>() {

    lateinit var pageSet: PageSet

    var page=1
    var pageSize="10"

    val layouts= mutableListOf<ParentLayout>()

    override fun getBinding(): ActivityListBinding {
       return ActivityListBinding.inflate(layoutInflater)
    }

    override fun initView() {

        pageSet = intent.getSerializableExtra(PageFlag) as PageSet

        viewBinding.listRefreshLayout.setOnRefreshListener {
            page=1
            load(true)
        }
        viewBinding.listRefreshLayout.setOnLoadMoreListener {
            page++
            load(false)
        }

        load(false)

    }

    fun load(clear:Boolean): Unit {
        val pageName= pageSet.load!!.loadParams.find { it.name=="PageName" }
        if (pageName!=null){
            pageName.def=page.toString()
        }
        val pageSizeName= pageSet.load!!.loadParams.find { it.name=="PageSizeName" }
        if (pageSizeName!=null){
            pageSizeName.def=pageSize
        }
        http(pageSet.load, {
            if (clear){
                layouts.clear()
            }
            val list = it.tryGet<ArrayList<MutableMap<String, Any>>>(pageSet.extra["rowName"].toString())
            if (list!=null){
                for (m in list){
                    layouts.add(ParentLayout(this))
                }
            }
            if (viewBinding.listRefresh.adapter==null){
                viewBinding.listRefresh.adapter = SimpleBindAdapter(
                    this@ListActivity,
                    ItemListBinding::class.java,
                    layouts
                ) { layout, bind ->
                    layout.show(bind.root,pageSet.lineSets,list!![layouts.indexOf(layout)])
                }
            }else{
                (viewBinding.listRefresh.adapter as BaseAdapter).notifyDataSetChanged()
            }

        }, { "连接超时，请确认网络状态是否联通".toast(this@ListActivity) })
    }
}