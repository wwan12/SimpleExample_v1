package com.aisino.tool.widget.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup



/**
 * 文件描述：
 * 作者：Administrator
 * 创建时间：2018/9/11/011
 * 更改时间：2018/9/11/011
 * 版本号：1
 *
 */
class ImageAdapter(val views: List<View>):PagerAdapter() {
    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1
    }

    //有多少个切换页
    override fun getCount(): Int {
        return views.size
    }

    //对超出范围的资源进行销毁
    override fun destroyItem(container: ViewGroup, position: Int,
                             `object`: Any) {
        //super.destroyItem(container, position, object);
        container.removeView(views[position])
    }

    //对显示的资源进行初始化
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //return super.instantiateItem(container, position);
        container.addView(views[position])
        return views[position]
    }

}