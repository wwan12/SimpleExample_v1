package com.hq.tool.widget.view.swipe.interfaces

interface SwipeAdapterInterface {
    fun getSwipeLayoutResourceId(position: Int): Int
    fun notifyDatasetChanged()
}