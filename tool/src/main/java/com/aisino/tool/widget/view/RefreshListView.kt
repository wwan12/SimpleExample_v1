package com.aisino.tool.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.aisino.tool.R
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.aisino.tool.widget.adapter.RefreshAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout


class RefreshListView : LinearLayout {
    constructor(context: Context) : super(context) { initView(context) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { initView(context)}
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) { initView(context)}

    lateinit var recyclerView:RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager
    var onRefresh:(refreshLayout: RefreshLayout)->Unit={}
    var onLoad:(refreshLayout: RefreshLayout)->Unit={}
    var waitTime = 0

    fun initView(context: Context): Unit {
        LayoutInflater.from(context).inflate(R.layout.listview_refresh,this);
        val refreshLayout = findViewById<SmartRefreshLayout>(R.id.refreshLayout)
        recyclerView = findViewById(R.id.rv_test)
        //设置分隔线
//        recyclerView.addItemDecoration(DividerGridItemDecoration(this))
//设置增加或删除条目的动画
        recyclerView.setItemAnimator(DefaultItemAnimator())
        addView(refreshLayout)
        refreshLayout.setOnRefreshListener{ refreshlayout ->
                refreshlayout.finishRefresh(waitTime/*,false*/)//传入false表示刷新失败
            onRefresh(refreshlayout)
            }

        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            refreshlayout.finishLoadMore(waitTime/*,false*/)//传入false表示加载失败
            onLoad(refreshlayout)
        }
    }

    /**
     * 设置数据，item id，item回调
     */
    fun <holder:RefreshAdapter.ViewHolder,E>setHolderAndEvent(data:List<E>,itemID:Int,onHolder:(holder: holder,data:E,position: Int)->Unit): Unit{
        if (layoutManager==null){
            layoutManager = LinearLayoutManager(context)
            //设置为垂直布局，这也是默认的
            (layoutManager as LinearLayoutManager).orientation = OrientationHelper.VERTICAL
            recyclerView.layoutManager=layoutManager
        }

        recyclerView.setAdapter(RefreshAdapter<holder,E>(data,itemID,onHolder))
    }

    fun setLayout(layoutManager:RecyclerView.LayoutManager): Unit {
        this.layoutManager=layoutManager
        recyclerView.layoutManager=layoutManager
    }

    fun addItemDecoration(): Unit {

    }

    fun setItemAnimator(): Unit {

    }

    fun setLoadTime(s:Int): Unit {
        waitTime=s
    }

    fun setOnRefreshListener(onRefresh:(refreshLayout: RefreshLayout)->Unit): Unit {
        this.onRefresh=onRefresh
    }

    fun setOnLoadMoreListener(onLoad:(refreshLayout: RefreshLayout)->Unit): Unit {
        this.onLoad=onLoad
    }

}