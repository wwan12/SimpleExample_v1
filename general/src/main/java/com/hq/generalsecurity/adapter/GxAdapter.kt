package com.hq.generalsecurity.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import com.hq.generalsecurity.expand.GX
import com.hq.generalsecurity.expand.GXData
import com.hq.generalsecurity.R
import kotlinx.android.synthetic.main.item_gx.view.*
import java.util.ArrayList

class GxAdapter(val activity: AppCompatActivity,val select:(GXData)->Unit):BaseAdapter() {

    val firstList:MutableList<GXData> = mutableListOf()
    val secondList:ArrayList<Data> = arrayListOf()
    val thirdList:ArrayList<Data> = arrayListOf()

    private lateinit var activeList:MutableList<GXData>
    var activeData: GXData?=null
    var parentDatas:MutableList<GXData> =mutableListOf()

    override fun getCount(): Int {
        return activeList.size
    }

    override fun getItem(i: Int): Any {
        return activeList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = activity.layoutInflater.inflate(R.layout.item_gx, null,false)
        v.gx_name.text=activeList[position].label
        v.setOnClickListener {
            if (activeData!=null){
                parentDatas.add(activeData!!)
            }
            activeData=activeList[position]
            select(activeList[position])
            if (!forward()){
               // select(activeList[position])
            }

        }
        return v
    }

    fun init(gx: GX): Unit {
        firstList.addAll(gx.data[0].children!!)
        activeList= mutableListOf()
        activeList.addAll(firstList)
    }

    fun open(): Unit {
        activeList.clear()
        activeList.addAll(firstList)
        parentDatas.clear()
        activeData=null
        notifyDataSetChanged()
    }

    fun forward(): Boolean {
        return if (activeData!!.children!=null&&activeData!!.children!!.size>0){
            activeList.clear()
            activeList.addAll(activeData!!.children!!)
            notifyDataSetChanged()
            true
        }else{
            false
        }
    }
    fun back(): Boolean {
        return if (parentDatas.size>0){
            activeData=parentDatas[parentDatas.size-1]
            parentDatas.removeAt(parentDatas.size-1)
            activeList.clear()
            activeList.addAll(activeData!!.children!!)
            notifyDataSetChanged()
            true
       }else{
           false
       }

    }

    data class Data(var id:String,var label:String,var children:MutableList<Data>?)
}