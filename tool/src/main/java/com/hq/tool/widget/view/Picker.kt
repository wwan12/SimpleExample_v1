package com.hq.tool.widget.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class Picker : View {
    private var mDataList: MutableList<GetConfigReq>? = null

    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private var mCurrentSelected = 0
    private var mPaint: Paint? = null
    private var mMaxTextSize = 20f
    private var mMinTextSize = 10f
    private val mMaxTextAlpha = 255f
    private val mMinTextAlpha = 120f
    private val mColorText = 0x333333
    private var mViewHeight = 0
    private var mViewWidth = 0
    private var mLastDownY = 0f

    /**
     * 滑动的距离
     */
    private var mMoveLen = 0f
    private var isInit = false
    private var mSelectListener: onSelectListener? = null
    private var timer: Timer? = null
    private var mTask: MyTimerTask? = null
    var hasLine=true

    var updateHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0f
                if (mTask != null) {
                    mTask?.cancel()
                    mTask = null
                    performSelect()
                }
            } else  // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLen -= mMoveLen / abs(mMoveLen) * SPEED
            invalidate()
        }
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    fun setOnSelectListener(listener: onSelectListener?) {
        mSelectListener = listener
        if (mDataList!!.size>0){
            mSelectListener?.defSelect(mDataList!![mCurrentSelected])
        }
    }

    private fun performSelect() {
        if (mSelectListener != null) mSelectListener!!.onSelect(mDataList!![mCurrentSelected])
    }

    fun setData(datas: MutableList<GetConfigReq>) {
        mDataList = datas
        mCurrentSelected = datas.size / 2
        invalidate()

    }

    /**
     * 选择选中的item的index
     *
     * @param selected
     */
    fun setSelected(selected: Int) {
        mCurrentSelected = selected
        val distance = mDataList!!.size / 2 - mCurrentSelected
        if (distance < 0) for (i in 0 until -distance) {
            moveHeadToTail()
            mCurrentSelected--
        } else if (distance > 0) for (i in 0 until distance) {
            moveTailToHead()
            mCurrentSelected++
        }
        invalidate()
    }

    /**
     * 选择选中的内容
     *
     * @param mSelectItem
     */
    fun setSelected(mSelectItem: String?) {
        for (i in mDataList!!.indices) if (mDataList!![i].equals(mSelectItem)) {
            setSelected(i)
            break
        }
    }

    private fun moveHeadToTail() {
        val datasBean: GetConfigReq = mDataList!![0]
        mDataList!!.removeAt(0)
        mDataList!!.add(datasBean)
    }

    private fun moveTailToHead() {
        if(mDataList!!.size>0){
            val datasBean: GetConfigReq = mDataList!![mDataList!!.size - 1]
            mDataList!!.removeAt(mDataList!!.size - 1)
            mDataList!!.add(0, datasBean)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewHeight = getMeasuredHeight()
        mViewWidth = getMeasuredWidth()
        // 按照View的高度计算字体大小
        mMaxTextSize = mViewHeight / 8.0f
        mMinTextSize = mMaxTextSize / 2f
        isInit = true
        invalidate()
    }

    private fun init() {
        timer = Timer()
        mDataList = ArrayList()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.setStyle(Paint.Style.FILL)
        mPaint?.setTextAlign(Paint.Align.CENTER)
        mPaint?.setColor(mColorText)

    }

   override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 根据index绘制view
        if (isInit&&mDataList!!.size>0) drawData(canvas)
    }

    private fun drawData(canvas: Canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        val scale = parabola(mViewHeight / 4.0f, mMoveLen)
        val size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize
        mPaint?.setTextSize(size)
        mPaint?.setAlpha(((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha).toInt())
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        val x = (mViewWidth / 2.0).toFloat()
        val y = (mViewHeight / 2.0 + mMoveLen).toFloat()
        val fmi: Paint.FontMetricsInt = mPaint!!.getFontMetricsInt()
        val baseline = (y - (fmi.bottom / 2.0f + fmi.top / 2.0f))
        val indexs = mCurrentSelected
        val textData: String = mDataList!![indexs].msg
        canvas.drawText(textData, x, baseline, mPaint!!)

        // 绘制上方data
        run {
            var i = 1
            while (mCurrentSelected - i >= 0) {
                drawOtherText(canvas, i, -1)
                i++
            }
        }
        // 绘制下方data
        var i = 1
        while (mCurrentSelected + i < mDataList!!.size) {
            drawOtherText(canvas, i, 1)
            i++
        }
    }

    /**
     * @param canvas
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private fun drawOtherText(canvas: Canvas, position: Int, type: Int) {
        val d =
            (MARGIN_ALPHA * mMinTextSize * position + type
                    * mMoveLen)
        val scale = parabola(mViewHeight / 4.0f, d)
        val size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize
        mPaint?.setTextSize(size)
        mPaint?.setAlpha(((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha).toInt())
        val y = (mViewHeight / 2.0 + type * d).toFloat()
        val fmi: Paint.FontMetricsInt = mPaint!!.getFontMetricsInt()
        val baseline = (y - (fmi.bottom / 2.0f + fmi.top / 2.0f))
        val indexs = mCurrentSelected + type * position
        val textData: String = mDataList!![indexs].msg
        canvas.drawText(textData, (mViewWidth / 2.0).toFloat(), baseline, mPaint!!)
//        if (hasLine){
//            canvas.drawLine(mViewWidth .toFloat(),baseline-10,(mViewWidth / 2.0+100).toFloat(),baseline-10,mPaint!!)
//        }
    }

    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     * @return scale
     */
    private fun parabola(zero: Float, x: Float): Float {
        val f = (1 - Math.pow(x / zero.toDouble(), 2.0)).toFloat()
        return if (f < 0) 0f else f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.getActionMasked()) {
            MotionEvent.ACTION_DOWN -> doDown(event)
            MotionEvent.ACTION_MOVE -> doMove(event)
            MotionEvent.ACTION_UP -> doUp(event)
        }
        return true
    }

    private fun doDown(event: MotionEvent) {
        if (mTask != null) {
            mTask?.cancel()
            mTask = null
        }
        mLastDownY = event.getY()
    }

    private fun doMove(event: MotionEvent) {
        mMoveLen += event.getY() - mLastDownY
        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
            // 往下滑超过离开距离
            moveTailToHead()
            mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
            // 往上滑超过离开距离
            moveHeadToTail()
            mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize
        }
        mLastDownY = event.getY()
        invalidate()
    }

    private fun doUp(event: MotionEvent) {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0f
            return
        }
        if (mTask != null) {
            mTask?.cancel()
            mTask = null
        }
        mTask = MyTimerTask(updateHandler)
        timer?.schedule(mTask, 0, 10)
    }

    internal inner class MyTimerTask(handler: Handler) : TimerTask() {
        var handler: Handler
        override fun run() {
            handler.sendMessage(handler.obtainMessage())
        }

        init {
            this.handler = handler
        }
    }

    interface onSelectListener {
        fun onSelect(pickers: GetConfigReq)
        fun defSelect(pickers: GetConfigReq)
    }

    companion object {
        const val TAG = "PickerScrollView:"

        /**
         * text之间间距和minTextSize之比
         */
        const val MARGIN_ALPHA = 2.8f

        /**
         * 自动回滚到中间的速度
         */
        const val SPEED = 2f
    }

    data class GetConfigReq ( var id:String, var msg: String,  var data:String?=null)
}