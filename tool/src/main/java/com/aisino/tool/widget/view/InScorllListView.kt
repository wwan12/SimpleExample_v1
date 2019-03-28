package com.aisino.tool.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ListView

/**
 * 可以滑动子控件的listview
 */
class InScorllListView : ListView {

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    /**
     * 改写MotionEvent 拦截条件,当子View中有需要请求nested scroll 的时候不进行拦截
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var superIntecept: Boolean
        val actionMasked = ev.actionMasked
//先不改变ListView的默认实现，在下面的代码中根据情况进行拦截修改
        superIntecept = super.onInterceptTouchEvent(ev)
        //当进行移动，且有子View需nested scroll,则一定不拦截
        when (actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                //选择性的拦截-有需要配合nested scroll的子View则不拦截，否则拦截
                //nestedScrollAxes默认为SCROLL_AXIS_NONE=0，即没有子View调用startNestedScroll向其进行nestedScroll的请求
                val parent = parent
                parent?.requestDisallowInterceptTouchEvent(true)
                return false
            }
        }
        return superIntecept
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2,
                MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }


}