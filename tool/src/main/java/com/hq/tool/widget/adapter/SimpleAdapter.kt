package com.hq.tool.widget.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class SimpleAdapter<T>(val activity: Activity,val itemId:Int,val list: List<T>,val view:(T,View)->Unit):BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]!!
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
      val v= activity.layoutInflater.inflate(itemId,null,false)
        view(list[position],v)
        return  v
    }
}