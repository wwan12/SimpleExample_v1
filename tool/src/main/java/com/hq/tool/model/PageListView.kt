package com.hq.tool.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.ListView
import androidx.core.view.get
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader

//自动控制页码，回调后自动刷新  todo 托管数据

class PageListView(context: Context, attrs: AttributeSet? = null):LinearLayout(context,attrs) {

    var pageNo=1

    var pageNumber=10

    private var listSet_p=ListSet(android.R.color.darker_gray,4)

    var list:ListView

    var refreshCall:(rLayout:RefreshLayout)->Unit={}
    var LoadCall:(rLayout:RefreshLayout)->Unit={}

    public var depositList:MutableList<Any>?=null

    public var listSet:ListSet
        get() = listSet_p
        set(value) {
            listSet_p=value
            setList(listSet_p)
        }


    init {
        val child=getChildAt(0)
        if (child!=null&&child is ListView){
            list=child
        }else{
            list=ListView(context)
        }
        val srl= SmartRefreshLayout(context)
        srl.addView(ClassicsHeader(context))
        srl.addView(list)
        srl.addView(ClassicsFooter(context))
        srl.setOnRefreshListener { refreshlayout ->
            refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            depositList?.clear()
            pageNo=1
            refreshCall(refreshlayout)
            (list.adapter as BaseAdapter).notifyDataSetChanged()
        }
        srl.setOnLoadMoreListener { refreshlayout ->
            refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
            pageNo++
            LoadCall(refreshlayout)
            (list.adapter as BaseAdapter).notifyDataSetChanged()
        }
        addView(srl)
    }

    fun setList(set:ListSet): Unit {
        list.divider= getDrawable(context.resources.getColor(set.divider),0)
        list.dividerHeight=set.dividerHeight
    }

    fun getDrawable(color: Int, cornerRadius: Int): Drawable? {
        val drawable = GradientDrawable()
        drawable.cornerRadius = cornerRadius.toFloat() //设置4个角的弧度
        drawable.setColor(color) // 设置颜色
        return drawable
    }

    data class ListSet(var divider:Int,var dividerHeight: Int)
}