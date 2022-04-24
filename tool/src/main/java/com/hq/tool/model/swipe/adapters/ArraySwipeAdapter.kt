package com.hq.tool.model.swipe.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.hq.tool.model.swipe.SwipeLayout
import com.hq.tool.model.swipe.implments.SwipeItemMangerImpl
import com.hq.tool.model.swipe.interfaces.SwipeAdapterInterface
import com.hq.tool.model.swipe.interfaces.SwipeItemMangerInterface
import com.hq.tool.model.swipe.util.Attributes

abstract class ArraySwipeAdapter<T> : ArrayAdapter<Any?>, SwipeItemMangerInterface, SwipeAdapterInterface {
    private val mItemManger = SwipeItemMangerImpl(this)

    constructor(context: Context, resource: Int) : super(context, resource) {}
    constructor(context: Context, resource: Int, textViewResourceId: Int) : super(context, resource, textViewResourceId) {}
    constructor(context: Context, resource: Int, objects: Array<T>?) : super(context, resource, objects) {}
    constructor(context: Context, resource: Int, textViewResourceId: Int, objects: Array<T>?) : super(context, resource, textViewResourceId, objects) {}
    constructor(context: Context, resource: Int, objects: List<T>?) : super(context, resource, objects) {}
    constructor(context: Context, resource: Int, textViewResourceId: Int, objects: List<T>?) : super(context, resource, textViewResourceId, objects) {}

    override fun notifyDatasetChanged() {
        super.notifyDataSetChanged()
    }

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