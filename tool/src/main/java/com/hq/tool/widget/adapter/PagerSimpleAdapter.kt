package com.hq.tool.widget.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.PagerAdapter

class PagerSimpleAdapter(val context: Context,val list: List<ViewBinding>,val titles: Array<String>):PagerAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view==any
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(list[position].root)
        return list[position].root
    }

    override fun destroyItem(container: ViewGroup, position: Int,any: Any) {
        //super.destroyItem(container, position, `object`)
        container.removeView(any as View?)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}