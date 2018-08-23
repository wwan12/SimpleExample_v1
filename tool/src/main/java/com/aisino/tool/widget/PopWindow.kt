package com.aisino.tool.widget

import android.app.Activity
import android.view.View
import android.widget.*
import com.aisino.tool.R

fun Activity.openUnterTheViewListWindow(view: View,data: ArrayList<String>,itemRun:(i: Int)->Unit):PopupWindow{
    // 将布局文件转换成View对象，popupview 内容视图
    val mPopView = this.layoutInflater.inflate(R.layout.under_the_view_window, null)
    // 将转换的View放置到 新建一个popuwindow对象中

    val mPopupWindow = PopupWindow(mPopView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
    // 点击popuwindow外让其消失
    mPopupWindow.setOutsideTouchable(true)
    val list= mPopView.findViewById<ListView>(R.id.under_the_view_list)
    list.adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,data)

    mPopupWindow.setOnDismissListener {
//        val params = this.getWindow().getAttributes()
//        params.alpha = 1f
//        this.getWindow().setAttributes(params)
    }
    if (mPopupWindow.isShowing()) {
        mPopupWindow.dismiss();
    } else {
        // 设置PopupWindow 显示的形式 底部或者下拉等
        // 在某个位置显示
        mPopupWindow.showAsDropDown(view,0,0)
        // 作为下拉视图显示
        // mPopupWindow.showAsDropDown(mPopView, Gravity.CENTER, 200, 300);
    }
    list.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
        itemRun(i)
        mPopupWindow.dismiss()
    }
    return mPopupWindow
}

fun ImageView.showFullWindow(): PopupWindow {
    // 将布局文件转换成View对象，popupview 内容视图
    val mPopView = (this.context as Activity).layoutInflater.inflate(R.layout.image_full_window, null)
    // 将转换的View放置到 新建一个popuwindow对象中
    val mPopupWindow = PopupWindow(mPopView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
    // 点击popuwindow外让其消失
    mPopupWindow.setOutsideTouchable(true)
    val view= mPopView.findViewById<ImageView>(R.id.show_full_window)
   view.setImageDrawable( this.drawable)
    view.setOnClickListener{
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
    mPopupWindow.showAsDropDown(this)
    return mPopupWindow
}