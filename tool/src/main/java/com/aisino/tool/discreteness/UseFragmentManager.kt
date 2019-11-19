package com.aisino.tool.discreteness


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

object UseFragmentManager {
    /**
     * 重新加载一个新的fragment
     *
     * @param fragments
     * @param fragment
     * @param fm
     * @param containerViewId
     */
    fun displayFragment(from: Fragment?, to: Fragment, fm: FragmentManager,
                        containerViewId: Int) {
        val ft = fm.beginTransaction()

        if (from == null) {
            ft.add(containerViewId, to)
            ft.show(to)
            ft.commitAllowingStateLoss()
        } else {
            ft.replace(containerViewId, to)
            ft.commitAllowingStateLoss()
        }

    }

    /**
     * 显示一个缓存的fragment
     */

    fun showFragment(from: Fragment?, to: Fragment, fm: FragmentManager,
                     containerViewId: Int): Unit {
        val ft = fm.beginTransaction()
        if (from!=null){
            ft.hide(from)
//            ft.remove(from)
//
        }
        if (to.isAdded) {
            ft.show(to)
            ft.commit()
        } else {
            ft.add(containerViewId, to)
            ft.show(to)
            ft.commit()
        }
    }

    /**
     * 隐藏Fragments
     *
     * @param fragments
     * @param ft
     */
    fun hideFragments(fragments: List<Fragment>, ft: FragmentTransaction) {
        for (i in fragments.indices) {
            if (fragments[i] != null) {
                ft.hide(fragments[i])
            }
        }
    }
}
