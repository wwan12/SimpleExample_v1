package com.aisino.jwtydjw.uav

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter




class AutoCompleteAdapter(context: Context, resource: Int,val list: ArrayList<String>) :ArrayAdapter<String>(context,resource,list){

    private var mFilter: ArrayFilter? = null
    val showList: ArrayList<String> = arrayListOf()

    override fun getCount(): Int {
        return showList.size
    }

    override fun getItem(position: Int): String? {
        return showList[position]
    }

    override fun getFilter(): Filter {
        if (mFilter==null){
            mFilter= ArrayFilter(this,showList,list)
        }
        return mFilter!!
    }

     class ArrayFilter (val adapter: AutoCompleteAdapter,val showList:ArrayList<String>,val list: ArrayList<String>): Filter() {
         override fun performFiltering(constraint: CharSequence): FilterResults {
             val results = FilterResults()
             if (constraint.isEmpty()) {
                 val list: ArrayList<String> = ArrayList(list)
                 results.values = list
                 results.count = list.size
             } else {
                 val prefixString = constraint.toString().toLowerCase() //大写换成小写
                 val values: ArrayList<String> = ArrayList(list) //未过滤前的List
                 val count: Int = values.size
                 val newValues: ArrayList<String> = ArrayList() //被过滤后的匹配的List
                 /*以下为过滤的条件,可按照个人的需求修改*/for (i in 0 until count) {
                     val valueText: String = values[i]//ValueText是每一项筛选的依据
                     if (valueText.startsWith(prefixString)) {
                         newValues.add(valueText)
                     } else {
                         if (valueText.contains(prefixString)) {
                             newValues.add(valueText)
                         }
                     }
                 }
                 results.values = newValues
                 results.count = newValues.size
             }
             return results
         }

         override fun publishResults(constraint: CharSequence, results: FilterResults) {
             //发表过滤的结果
             showList.clear()
             showList.addAll(results.values as ArrayList<String>)
             if (results.count > 0) {
                 adapter.notifyDataSetChanged()
             } else {
                 adapter.notifyDataSetInvalidated()
             }
         }
     }

}