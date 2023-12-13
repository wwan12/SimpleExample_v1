package com.hq.tool.widget.view.swipe.adapters

import android.content.Context
import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleCursorAdapter
import com.hq.tool.widget.view.swipe.SwipeLayout
import com.hq.tool.widget.view.swipe.implments.SwipeItemMangerImpl
import com.hq.tool.widget.view.swipe.interfaces.SwipeAdapterInterface
import com.hq.tool.widget.view.swipe.interfaces.SwipeItemMangerInterface
import com.hq.tool.widget.view.swipe.util.Attributes

abstract class SimpleCursorSwipeAdapter : SimpleCursorAdapter, SwipeItemMangerInterface, SwipeAdapterInterface {
    private val mItemManger = SwipeItemMangerImpl(this)

    protected constructor(context: Context?, layout: Int, c: Cursor?, from: Array<String?>?, to: IntArray?, flags: Int) : super(context, layout, c, from, to, flags) {}
    protected constructor(context: Context?, layout: Int, c: Cursor?, from: Array<String?>?, to: IntArray?) : super(context, layout, c, from, to) {}

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val v = super.getView(position, convertView, parent)
        mItemManger.bind(v, position)
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