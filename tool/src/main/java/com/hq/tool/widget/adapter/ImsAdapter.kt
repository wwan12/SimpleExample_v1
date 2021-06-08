package com.hq.tool.widget.adapter

import android.R.attr.data
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.aisino.jyzy.R
import com.aisino.jyzy.jyz.JyzQueryActivity
import kotlinx.android.synthetic.main.item_spinner_text.view.*

//
//class ImsAdapter(val activity: Activity, val list: ArrayList<JyzQueryActivity.ImsItem>): BaseAdapter(), Filterable {
//    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
//        val view=activity.layoutInflater.inflate(R.layout.item_spinner_text,p2,false)
//        view.textView.text=list[p0].stationName
//
//        return view
//    }
//
//    override fun getItem(p0: Int): Any {
//        return list[p0].stationName
//    }
//
//    override fun getItemId(p0: Int): Long {
//        return  p0.toLong()
//    }
//
//    override fun getCount(): Int {
//        return list.size
//    }
//
//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence): FilterResults {
//
//                val v=ArrayList<String>()
//                for (l in list){
//                    v.add(l.stationName)
//                }
//                return FilterResults().apply {
//                    values= v
//                    count=v.size
//                }
//            }
//
//            override fun publishResults(constraint: CharSequence, results: FilterResults) {
//             //   val data = results.values as ArrayList<String>
//                notifyDataSetChanged()
//            }
//        }
//    }
//
//}