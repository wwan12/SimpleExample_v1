package com.hq.generalsecurity.standardform

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayout
import com.hq.generalsecurity.BaseActivity
import com.hq.generalsecurity.databinding.ActivityViewPagerBinding
import com.hq.generalsecurity.expand.*
import com.hq.generalsecurity.expand.Expand.getJson
import com.hq.generalsecurity.expand.Expand.getPage
import com.hq.generalsecurity.set.Flag
import com.hq.tool.widget.adapter.FragmentAdapter

class ViewPagerActivity:BaseActivity<ActivityViewPagerBinding>() {

    val adapter: FragmentAdapter? = null
    var fragments: ArrayList<Fragment> = ArrayList()

    override fun getBinding(): ActivityViewPagerBinding {
        return ActivityViewPagerBinding.inflate(layoutInflater, baseBinding.container, true)
    }

    override fun initView() {

       val pageSet=intent.getSerializableExtra(Flag.PAGE_FLAG) as ViewPagerStandardPage

        setTitle(pageSet.pageName)

        val titles= Array(pageSet.pageNames.size) { "" }

        for (i in 0 until pageSet.pageNames.size){
            val pageName=pageSet.pageNames[i].pageName
            getPage(pageName){ page->
                titles[i]=page!!.pageName
                when(page.pageType){
                    PageType.FormStandard->{
                        fragments.add(StandardFormFragment(page as FormStandardPage))
                    }
                    PageType.ListStandard->{
                        fragments.add(ListFragment<MutableMap<*,*>,ViewBinding>(page as ListStandardPage))
                    }
                }
            }
        }

        viewBinding.viewpager.adapter = FragmentAdapter(supportFragmentManager, fragments, titles)
        viewBinding.viewpager.offscreenPageLimit = fragments.size - 1
        viewBinding.viewpager.addOnPageChangeListener(object :
            TabLayout.TabLayoutOnPageChangeListener( viewBinding.tabLayout) {})
        viewBinding.tabLayout.setupWithViewPager( viewBinding.viewpager)
        viewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null)
                    viewBinding.viewpager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

}