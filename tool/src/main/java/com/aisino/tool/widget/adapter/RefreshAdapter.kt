package com.aisino.tool.widget.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



class RefreshAdapter<holder: RefreshAdapter.ViewHolder,E>(val data:List<E>, val itemID:Int, val onholder:(holder: holder,data:E,position: Int)->Unit) : RecyclerView.Adapter<RefreshAdapter.ViewHolder>() {

class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

}
    override fun onBindViewHolder(holder: RefreshAdapter.ViewHolder, position: Int) {
        onholder(holder as holder,data[position],position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefreshAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.getContext()).inflate(itemID, parent, false)
        return ViewHolder(v)
    }
}