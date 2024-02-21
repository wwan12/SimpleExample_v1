package com.hq.general.standardform

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayout
import com.hq.general.BaseActivity
import com.hq.general.databinding.ActivityViewPagerBinding
import com.hq.general.expand.Expand.getPage
import com.hq.general.model.FormStandardPage
import com.hq.general.model.ListStandardPage
import com.hq.general.model.PageType
import com.hq.general.model.ViewPagerStandardPage
import com.hq.general.set.Flag
import com.hq.tool.widget.adapter.FragmentAdapter

class ViewPagerActivity: BaseActivity<ActivityViewPagerBinding>() {

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
            titles[i]=pageSet.pageNames[i].name
            pageSet.pageNames[i].getPage(this){page->
                when(page!!.pageType){
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
            TabLayout.TabLayoutOnPageChangeListener( viewBinding.tabLayout) {

            })
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