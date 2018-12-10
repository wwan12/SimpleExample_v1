package com.aisino.tool.widget

import android.app.Activity
import android.content.ComponentCallbacks
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.*
import com.aisino.tool.R
import java.lang.Comparable
import java.text.FieldPosition
import android.support.v4.view.ViewPager
import android.view.*

/**
 * 显示一个在当前view下方的listpopwindow
 */
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
    mPopView.setOnKeyListener { view, i, keyEvent ->
        if (i == KeyEvent.KEYCODE_BACK) {//返回自动关闭pop
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
                return@setOnKeyListener true
            }
        }
        return@setOnKeyListener false
    }
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

/**
 * 将当前imageview放大到全屏显示
 */
fun ImageView.showFullWindow(): PopupWindow {
    // 将布局文件转换成View对象，popupview 内容视图
    val mPopView = (this.context as Activity).layoutInflater.inflate(R.layout.image_full_window, null)
    // 将转换的View放置到 新建一个popuwindow对象中
    val mPopupWindow = PopupWindow(mPopView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
    // 点击popuwindow外让其消失
    mPopupWindow.setOutsideTouchable(true)
    mPopView.setOnKeyListener { view, i, keyEvent ->
        if (i == KeyEvent.KEYCODE_BACK) {//返回自动关闭pop
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss()
                return@setOnKeyListener true
            }
        }
        return@setOnKeyListener false
    }
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

/**
 * 显示一个可以左右滑动的画廊
 */
fun Activity.showGrallery(position:Int,imgs:List<Bitmap>): PopupWindow {
    val views=ArrayList<View>()
    for (img in imgs){
        val newImg= ImageView(this)
        newImg.setImageBitmap(img)
        newImg.layoutParams= ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
        newImg.scaleType=ImageView.ScaleType.FIT_CENTER
        views.add(newImg)
    }
    val mPopView= this.layoutInflater.inflate(R.layout.image_full_window, null)
    val viewPager = mPopView.findViewById(R.id.pager) as ViewPager

    val myPagerAdapter = ImageAdapter(views)
    viewPager.adapter = myPagerAdapter
    viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {

        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    })
    viewPager.currentItem=position
    val mPopupWindow = PopupWindow(mPopView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
    // 点击popuwindow外让其消失
    mPopupWindow.setOutsideTouchable(true)
    mPopView.setOnClickListener{
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
    mPopView.setOnKeyListener { view, i, keyEvent ->
        if (i == KeyEvent.KEYCODE_BACK) {//返回自动关闭pop
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss()
                return@setOnKeyListener true
            }
        }
        return@setOnKeyListener false
    }
    mPopupWindow.showAsDropDown(views[position])
    return mPopupWindow
}

/**
 * 显示一个2级菜单
 */
fun showSecondLevelPop(context: Activity, leftList: List<String>, rightList: List<List<String>>,itemRun:(left:String,right:String,i: Int)->Unit) {
    val linearLayout = LinearLayout(context)
    linearLayout.orientation = LinearLayout.HORIZONTAL
    linearLayout.setBackgroundColor(Color.WHITE)
    val pop = PopupWindow(linearLayout,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true)
    pop.setBackgroundDrawable(ColorDrawable(0xffffff))//支持点击Back虚拟键退出
    val lpLeft = LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT).apply { weight = 1f }
    val lpRifght = LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT).apply { weight = 2f }
    val leftView = ListView(context)
    leftView.layoutParams = lpLeft
    val rightView = ListView(context)
    rightView.layoutParams = lpRifght
    leftView.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, leftList)
    val cacheRight = java.util.ArrayList<String>()
    cacheRight.addAll(rightList[0])
    rightView.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, cacheRight)
    linearLayout.addView(leftView)
    linearLayout.addView(rightView)
    val text = StringBuffer()
    text.append(leftList[0])
    leftView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
        cacheRight.clear()
        cacheRight.addAll(rightList[position])
        (rightView.adapter as BaseAdapter).notifyDataSetChanged()
        text.delete(0, text.length)
        text.append(leftList[position])
    }
    rightView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
        if (pop.isShowing) {
            pop.dismiss()
        }
        itemRun(text.toString(),cacheRight[position],position)
    }
    pop.setOnDismissListener {
        if (pop.isShowing) {
            pop.dismiss()
        }
        val params = context.window.attributes
        params.alpha = 1f
        context.window.attributes = params
    }
    val params = context.window.attributes
    params.alpha = 0.7f
    context.window.attributes = params
    pop.showAtLocation(context.currentFocus, Gravity.CENTER, 0, 0)
}