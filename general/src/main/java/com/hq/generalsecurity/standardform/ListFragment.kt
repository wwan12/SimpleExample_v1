package com.hq.generalsecurity.standardform

import android.widget.BaseAdapter
import androidx.viewbinding.ViewBinding
import com.hq.generalsecurity.BaseFragment
import com.hq.generalsecurity.databinding.FragmentListBinding
import com.hq.generalsecurity.databinding.ItemListBinding
import com.hq.generalsecurity.expand.ClickAction
import com.hq.generalsecurity.expand.Expand
import com.hq.generalsecurity.expand.ListStandardPage
import com.hq.generalsecurity.expand.PageSet
import com.hq.generalsecurity.set.Flag
import com.hq.tool.toStringPro
import com.hq.tool.toast
import com.hq.tool.widget.adapter.SimpleBindAdapter

class ListFragment<T,E: ViewBinding>(val listSet: ListStandardPage? =null,
    val cls:(Class<E>?)=null, val list: (List<T>?)=null, val viewCall:((T, E)->Unit)?=null):BaseFragment<FragmentListBinding>() {

    var page=0
    var pageSize=10

    val showList= arrayListOf<MutableMap<String,Any>>()

    override fun initView() {

        viewBinding.listRefreshLayout.setOnRefreshListener {
            page=1
            load(true)
        }
        viewBinding.listRefreshLayout.setOnLoadMoreListener {
            page++
            load(false)
        }

        if (cls!=null&&list!=null&&viewCall!=null){
            viewBinding.list.adapter=SimpleBindAdapter(requireActivity(),cls,list, viewCall)
        }else{
            viewBinding.list.adapter=SimpleBindAdapter(requireActivity(),ItemListBinding::class.java,showList)
            { t, e ->
                for (line in listSet!!.lineSets){
                    val p = line.place(requireActivity(),t)
                    when(line.position){
                        Flag.Dir.Left->   e.left.addView(p.viewBinding.root)
                        Flag.Dir.Right->  e.right.addView(p.viewBinding.root)
                        Flag.Dir.Top->  e.top.addView(p.viewBinding.root)
                        Flag.Dir.Down->  e.down.addView(p.viewBinding.root)
                    }
                }
            }
        }
        viewBinding.list.setOnItemClickListener { parent, view, position, id ->
           when(listSet?.onItemClick) {
               ClickAction.Go-> Expand.toFormActivity(requireActivity(),listSet.route)
           }
        }
        load(false)
    }


    override fun <E> setData(e: E) {

    }

    fun load(clear:Boolean): Unit {
        listSet?.load(page,pageSize,{
            if (clear){
                showList.clear()
            }
            showList.addAll(it)
            (viewBinding.list.adapter as BaseAdapter).notifyDataSetChanged()
        },{
            it.failMsg.toast(requireActivity())
        })
    }
}