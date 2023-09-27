package com.hq.tool.widget.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint

import android.content.Context

import android.graphics.*

import android.util.AttributeSet
import android.view.MotionEvent

import android.view.View

import androidx.core.content.ContextCompat
import com.hq.tool.loge
import java.lang.Math.abs

/**
 * Date: 2020/8/12 13:21
 * Description: 长按动画View
 *
 *
val touchMax = 50

var lastX = 0

var lastY = 0

circleView.setOnTouchListener( object : View.OnTouchListener {
override fun onTouch(p0: View?, motionEvent: MotionEvent): Boolean {
val endTime: Long

val x = motionEvent.x

val y = motionEvent.y

when (motionEvent.action) {
MotionEvent.ACTION_DOWN -> {
startTime = System.currentTimeMillis()

lastX = x.toInt()

lastY = y.toInt()

circleView.startAnim()

}

MotionEvent.ACTION_UP -> {
endTime = System.currentTimeMillis()

val during = endTime - startTime

if (during < App.LONG_CLICK_TIME) {
circleView.cancelAnim()

circleView.clearAll()

} else {
flashTV.text = "OK"

}

}

MotionEvent.ACTION_MOVE -> {
if (abs(lastX - x) > touchMax || abs(lastY - y) > touchMax) {
circleView.clearAll()

}

}

}

return false

}

})
 */

class LongClickRoundView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    /**

     * 画笔

     */

    private val paint = Paint()

    private var arcPath: Path? = null

    private var rectF: RectF? = null

    private var lineColor = 0

    /**

     * 中心点坐标、半径

     */

    private var centerX: Float? = null

    private var centerY: Float? = null

    private var radius: Float? = null

    private var left = -1F

    private var top = -1F

    private var right = -1F

    private var bottom = -1F

    private val offset = 10

    private var mProgress = -1F

    private var valueAnimator: ValueAnimator? = null

    private var isClear = true


    val touchMax = 50

    var lastX = 0

    var lastY = 0

    var startTime=0L

    var longClickTime=3000L


    var endTime=0L


    init {
//        lineColor = ContextCompat.getColor(context!!, R.color.red)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        centerX = width / 2.toFloat()

        centerY = height / 2.toFloat()

        radius = height / 2.toFloat()

        left = centerX!! - radius!! + offset

        top = centerY!! - radius!! + offset

        right = centerX!! + radius!! - offset

        bottom = centerY!! + radius!! - offset

        rectF = RectF(left, top, right, bottom)

        arcPath = Path()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.isAntiAlias = true

        paint.color = lineColor

        paint.style = Paint.Style.STROKE

        paint.strokeWidth = 5F

        arcPath!!.rewind() // 清除直线数据，保留数据结构，方便快速重用

        if (isClear) return

        arcPath!!.arcTo(rectF!!, 270F, mProgress)

        canvas?.drawPath(arcPath!!, paint)

    }

    fun startAnim() {
        isClear = false

        valueAnimator = ValueAnimator.ofFloat(0F, 359.9999F)

        valueAnimator!!.duration =longClickTime


        valueAnimator!!.addUpdateListener { animation ->

            mProgress = animation.animatedValue as Float

            invalidate()

        }

        valueAnimator!!.start()

    }

    fun cancelAnim() {
        valueAnimator!!.cancel()

    }

    fun clearAll() {
        isClear = true

        invalidate()

    }
    @SuppressLint("ClickableViewAccessibility")
    fun setOnListener(longTime:Long,color:Int,call:()->Unit): Unit {
        longClickTime=longTime

        lineColor=color

        paint.color = lineColor


        setOnTouchListener { p0, motionEvent ->

            motionEvent.action.toString().loge()

            val x = motionEvent.x

            val y = motionEvent.y

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    startTime = System.currentTimeMillis()

                    lastX = x.toInt()

                    lastY = y.toInt()

                    startAnim()

                }

                MotionEvent.ACTION_UP -> {
                    endTime = System.currentTimeMillis()

                    val during = endTime - startTime

                    if (during < longClickTime) {
                       cancelAnim()
                        clearAll()
                    } else {
                        cancelAnim()
                        clearAll()
                        call()
                    }

                }

                MotionEvent.ACTION_MOVE -> {
                    if (abs(lastX - x) > touchMax || abs(lastY - y) > touchMax) {
                        clearAll()
                    }
                }
            }

            true
        }
    }
}