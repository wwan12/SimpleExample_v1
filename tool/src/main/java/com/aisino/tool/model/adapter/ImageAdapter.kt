package com.aisino.tool.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aisino.tool.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ImageAdapter(val context: Context, val taskList: ArrayList<String>,val defImgId:Int,val errorId:Int) : BaseAdapter() {

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.item_image, null)
        var image = view.findViewById<ImageView>(R.id.show_image)
        Glide.with(context).load(taskList[p0]).apply(RequestOptions().placeholder(defImgId).error(errorId)).into(image)
        return view
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return taskList?.size!!
    }

}