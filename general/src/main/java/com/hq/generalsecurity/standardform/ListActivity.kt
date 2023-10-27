package com.hq.generalsecurity.standardform

import android.widget.BaseAdapter
import com.hq.generalsecurity.BaseActivity
import com.hq.generalsecurity.databinding.ActivityListBinding
import com.hq.generalsecurity.databinding.ItemListBinding
import com.hq.generalsecurity.expand.*
import com.hq.generalsecurity.expand.Expand.http
import com.hq.generalsecurity.widget.form.ParentLayout
import com.hq.generalsecurity.set.Flag
import com.hq.tool.toStringPro
import com.hq.tool.toast
import com.hq.tool.widget.adapter.SimpleBindAdapter

/**
 * 单列表页面
 */
class ListActivity: BaseActivity<ActivityListBinding>() {

    lateinit var pageSet: ListStandardPage

    var page=1
    var pageSize=10

    val layouts= mutableListOf<ParentLayout>()

    override fun getBinding(): ActivityListBinding {
       return ActivityListBinding.inflate(layoutInflater)
    }

    override fun initView() {

        pageSet = intent.getSerializableExtra(Flag.PAGE_FLAG) as ListStandardPage
        setTitle(pageSet.pageName)
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

        pageSet.load(page,pageSize,{
            if (clear){
                layouts.clear()
            }
            for (m in it){
                layouts.add(ParentLayout(this))
            }
            if (viewBinding.listRefresh.adapter==null){
                if (pageSet.theme!=null){

                }
                viewBinding.listRefresh.adapter = SimpleBindAdapter(
                    this@ListActivity,
                    ItemListBinding::class.java,
                    layouts
                ) { layout, bind ->
                    layout.show(bind.root,pageSet.lineSets,it[layouts.indexOf(layout)])
                }
            }else{
                (viewBinding.listRefresh.adapter as BaseAdapter).notifyDataSetChanged()
            }
        },{
            "连接超时，请确认网络状态是否联通".toast(this@ListActivity)
        })
    }
}