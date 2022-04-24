package com.hq.tool.model.swipe.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hq.tool.model.swipe.SwipeLayout
import com.hq.tool.model.swipe.implments.SwipeItemMangerImpl
import com.hq.tool.model.swipe.interfaces.SwipeAdapterInterface
import com.hq.tool.model.swipe.interfaces.SwipeItemMangerInterface
import com.hq.tool.model.swipe.util.Attributes

abstract class RecyclerSwipeAdapter<VH : RecyclerView.ViewHolder?> : RecyclerView.Adapter<VH>(), SwipeItemMangerInterface, SwipeAdapterInterface {
    @JvmField
    var mItemManger = SwipeItemMangerImpl(this)
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
    abstract override fun onBindViewHolder(viewHolder: VH, position: Int)
    override fun notifyDatasetChanged() {
        super.notifyDataSetChanged()
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