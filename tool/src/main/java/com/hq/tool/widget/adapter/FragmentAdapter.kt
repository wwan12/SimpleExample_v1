package com.hq.tool.widget.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *    viewpager.adapter= FragmentAdapter(supportFragmentManager, this, fragments, titles)
viewpager.offscreenPageLimit = fragments.size - 1
viewpager.addOnPageChangeListener(object : TabLayout.TabLayoutOnPageChangeListener(tabLayout){})
tabLayout.setupWithViewPager(viewpager)
tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener {
override fun onTabSelected(tab: TabLayout.Tab?) {
if (tab!=null)
viewpager.currentItem=tab.position
}

override fun onTabUnselected(tab: TabLayout.Tab?) {

}

override fun onTabReselected(tab: TabLayout.Tab?) {

}

})
 *
 */
class FragmentAdapter(private val context: Context, fm: FragmentManager, private val list: List<Fragment>, private val titles: Array<String>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}