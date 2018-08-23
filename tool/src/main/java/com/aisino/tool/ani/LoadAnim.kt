package com.aisino.tool.ani

/**
 * Created by lenovo on 2018/2/5.
 */

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator

import com.aisino.tool.R

class LoadAnim : View {

    private lateinit var mPaint: Paint

    private lateinit var loadingRectF: RectF
    private lateinit var shadowRectF: RectF

    private var topDegree = 10
    private var bottomDegree = 190

    private var arc: Float = 0f

    private var width0:Int = 0

    private var changeBigger = true

    private var shadowPosition: Int = 0

    var isStart = false
        private set

    var loadingColor: Int = 0

    private var speedOfDegree: Int = 0

    private var speedOfArc: Float = 0.toFloat()

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        loadingColor = Color.WHITE
        width0 = dpToPx(context, DEFAULT_WIDTH.toFloat())
        shadowPosition = dpToPx(getContext(), DEFAULT_SHADOW_POSITION.toFloat())
        speedOfDegree = DEFAULT_SPEED_OF_DEGREE

        if (null != attrs) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RotateLoading)
            loadingColor = typedArray.getColor(R.styleable.RotateLoading_loading_color, Color.WHITE)
            width0 = typedArray.getDimensionPixelSize(R.styleable.RotateLoading_loading_width, dpToPx(context, DEFAULT_WIDTH.toFloat()))
            shadowPosition = typedArray.getInt(R.styleable.RotateLoading_shadow_position, DEFAULT_SHADOW_POSITION)
            speedOfDegree = typedArray.getInt(R.styleable.RotateLoading_loading_speed, DEFAULT_SPEED_OF_DEGREE)
            typedArray.recycle()
        }
        speedOfArc = (speedOfDegree / 4).toFloat()
        mPaint = Paint()
        mPaint.color = loadingColor
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = width0.toFloat()
        mPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        arc = 10f
        loadingRectF = RectF((2 * width0).toFloat(), (2 * width0).toFloat(), (w - 2 * width0).toFloat(), (h - 2 * width0).toFloat())
        shadowRectF = RectF((2 * width0 + shadowPosition).toFloat(), (2 * width0 + shadowPosition).toFloat(), (w - 2 * width0 + shadowPosition).toFloat(), (h - 2 * width0 + shadowPosition).toFloat())
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!isStart) {
            return
        }

        mPaint.color = Color.parseColor("#1a000000")
        canvas.drawArc(shadowRectF, topDegree.toFloat(), arc, false, mPaint)
        canvas.drawArc(shadowRectF, bottomDegree.toFloat(), arc, false, mPaint)

        mPaint.color = loadingColor
        canvas.drawArc(loadingRectF, topDegree.toFloat(), arc, false, mPaint)
        canvas.drawArc(loadingRectF, bottomDegree.toFloat(), arc, false, mPaint)

        topDegree += speedOfDegree
        bottomDegree += speedOfDegree
        if (topDegree > 360) {
            topDegree = topDegree - 360
        }
        if (bottomDegree > 360) {
            bottomDegree = bottomDegree - 360
        }

        if (changeBigger) {
            if (arc < 160) {
                arc += speedOfArc
                invalidate()
            }
        } else {
            if (arc > speedOfDegree) {
                arc -= 2 * speedOfArc
                invalidate()
            }
        }
        if (arc >= 160 || arc <= 10) {
            changeBigger = !changeBigger
            invalidate()
        }
    }

    fun start() {
        startAnimator()
        isStart = true
        invalidate()
    }

    fun stop() {
        stopAnimator()
        invalidate()
    }

    private fun startAnimator() {
        val scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 0.0f, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 0.0f, 1f)
        scaleXAnimator.setDuration(300)
        scaleXAnimator.setInterpolator(LinearInterpolator())
        scaleYAnimator.setDuration(300)
        scaleYAnimator.setInterpolator(LinearInterpolator())
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator)
        animatorSet.start()
    }

    private fun stopAnimator() {
        val scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0f)
        val scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0f)
        scaleXAnimator.setDuration(300)
        scaleXAnimator.setInterpolator(LinearInterpolator())
        scaleYAnimator.setDuration(300)
        scaleYAnimator.setInterpolator(LinearInterpolator())
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator)
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                isStart = false
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        animatorSet.start()
    }


    fun dpToPx(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
    }

    companion object {

        private val DEFAULT_WIDTH = 6
        private val DEFAULT_SHADOW_POSITION = 2
        private val DEFAULT_SPEED_OF_DEGREE = 10
    }

}