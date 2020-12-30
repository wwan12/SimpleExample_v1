package com.hq.tool.model.swipe.interfaces

import com.hq.tool.model.swipe.SwipeLayout
import com.hq.tool.model.swipe.util.Attributes

interface SwipeItemMangerInterface {
    fun openItem(position: Int)
    fun closeItem(position: Int)
    fun closeAllExcept(layout: SwipeLayout)
    fun closeAllItems()
    val openItems: List<Int?>?
    val openLayouts: List<SwipeLayout?>?
    fun removeShownLayouts(layout: SwipeLayout?)
    fun isOpen(position: Int): Boolean
    fun Mode(): Attributes.Mode?
    fun Mode(mode: Attributes.Mode)
}