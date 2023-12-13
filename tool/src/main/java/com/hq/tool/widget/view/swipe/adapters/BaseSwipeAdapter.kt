package com.hq.tool.widget.view.swipe.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.hq.tool.widget.view.swipe.SwipeLayout
import com.hq.tool.widget.view.swipe.implments.SwipeItemMangerImpl
import com.hq.tool.widget.view.swipe.interfaces.SwipeAdapterInterface
import com.hq.tool.widget.view.swipe.interfaces.SwipeItemMangerInterface
import com.hq.tool.widget.view.swipe.util.Attributes

abstract class BaseSwipeAdapter : BaseAdapter(), SwipeItemMangerInterface, SwipeAdapterInterface {
    protected var mItemManger = SwipeItemMangerImpl(this)

    /**
     * return the [com.hq.tool.model.swipe.SwipeLayout] resource id, int the view item.
     * @param position
     * @return
     */
    abstract override fun getSwipeLayoutResourceId(position: Int): Int

    /**
     * generate a new view item.
     * Never bind SwipeListener or fill values here, every item has a chance to fill value or bind
     * listeners in fillValues.
     * to fill it in `fillValues` method.
     * @param position
     * @param parent
     * @return
     */
    abstract fun generateView(position: Int, parent: ViewGroup?): View

    /**
     * fill values or bind listeners to the view.
     * @param position
     * @param convertView
     */
    abstract fun fillValues(position: Int, convertView: View?)
    override fun notifyDatasetChanged() {
        super.notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var v = convertView
        if (v == null) {
            v = generateView(position, parent)
        }
        mItemManger.bind(v, position)
        fillValues(position, v)
        return v
    }

    override fun openItem(position: Int) {
        mItemManger.openItem(position)
    }

    override fun closeItem(position: Int) {
        mItemManger.closeItem(position)
    }

    override fun closeAllExcept(layout: SwipeLayout) {
        mItemManger.closeAllExcept(layout)
    }

    override fun closeAllItems() {
        mItemManger.closeAllItems()
    }

    override val openItems: List<Int?>?
        get() = mItemManger.openItems
    override val openLayouts: List<SwipeLayout?>?
        get() = mItemManger.openLayouts

    override fun removeShownLayouts(layout: SwipeLayout?) {
        mItemManger.removeShownLayouts(layout)
    }

    override fun isOpen(position: Int): Boolean {
        return mItemManger.isOpen(position)
    }

    override fun Mode(): Attributes.Mode? {
        return mItemManger.mode
    }

    override fun Mode(mode: Attributes.Mode) {
        mItemManger.mode = mode
    }
}