package com.hq.tool.model.swipe.interfaces

interface SwipeAdapterInterface {
    fun getSwipeLayoutResourceId(position: Int): Int
    fun notifyDatasetChanged()
}