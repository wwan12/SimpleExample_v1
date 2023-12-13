package com.hq.tool.widget.view.swipe

import com.hq.tool.widget.view.swipe.SwipeLayout.SwipeListener

open class SimpleSwipeListener : SwipeListener {
    override fun onStartOpen(layout: SwipeLayout) {}
    override fun onOpen(layout: SwipeLayout) {}
    override fun onStartClose(layout: SwipeLayout?) {}
    override fun onClose(layout: SwipeLayout?) {}
    override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {}
    override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {}
}