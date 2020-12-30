package com.hq.tool.widget.view

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView


//可手势缩放其中图片的imageview

public class MatrixImageView(context: Context, attrs: AttributeSet? = null) : AppCompatImageView(context, attrs), View.OnTouchListener{
    object ZoomMode {
        const val Ordinary = 0
        const val ZoomIn = 1
        const val DoubleZoomIn = 2
    }

    private var curMode = 0


    private  var matrix_t:Matrix?=null

    private lateinit var viewSize: PointF

    private lateinit var imageSize: PointF

    private  lateinit var scaleSize: PointF

    //记录图片当前坐标
    private lateinit var curPoint: PointF

    private lateinit var originScale: PointF

    //0:宽度适应 1:高度适应
    private var fitMode = 0

    private lateinit var start: PointF

    private lateinit var center: PointF

    private var scaleDoubleZoom = 0f

    private lateinit var relativePoint: PointF

    private var doubleFingerDistance = 0f

    var doubleClickTimeSpan: Long = 100

    var lastClickTime: Long = 0

    var rationZoomIn = 2

    init {
        GestureImageViewInit()
    }

    fun GestureImageViewInit() {
        setOnTouchListener(this)
        this.scaleType = ScaleType.MATRIX
        matrix_t = Matrix()
        originScale = PointF()
        scaleSize = PointF()
        start = PointF()
        center = PointF()
        curPoint = PointF()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        viewSize = PointF(width.toFloat(), height.toFloat())

        //获取当前Drawable的大小
        val drawable: Drawable? = drawable
        if (drawable == null) {
            Log.e("no drawable", "drawable is nullPtr")
        } else {
            imageSize = PointF(drawable.minimumWidth.toFloat(), drawable.minimumHeight.toFloat())
            FitCenter()
        }
    }

    /**
     * 使图片保存在中央
     */
    fun FitCenter() {
        val scaleH: Float = viewSize.y / imageSize.y
        val scaleW: Float = viewSize.x / imageSize.x
        //选择小的缩放因子确保图片全部显示在视野内
        val scale = if (scaleH < scaleW) scaleH else scaleW
        //根据view适应大小
        setImageScale(PointF(scale, scale))
        originScale.set(scale, scale)
        //根据缩放因子大小来将图片中心调整到view 中心
        if (scaleH < scaleW) {
            setImageTranslation(PointF(viewSize.x / 2 - scaleSize.x / 2, 0f))
            fitMode = 1
        } else {
            fitMode = 0
            setImageTranslation(PointF(0f, viewSize.y / 2 - scaleSize.y / 2))
        }

        //记录缩放因子 下次继续从这个比例缩放
        scaleDoubleZoom = originScale.x
    }





    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when (event.getAction() and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                start.set(event.getX(), event.getY())
                //手指按下事件
                if (event.pointerCount == 1) {
                    if (event.getEventTime() - lastClickTime <= doubleClickTimeSpan) {
                        //双击事件触发
                        Log.e("TouchEvent", "DoubleClick")
                        if (curMode == ZoomMode.Ordinary) {
                            curMode = ZoomMode.ZoomIn
                            relativePoint = PointF()
                            //计算归一化坐标
                            relativePoint?.set(
                                (start.x - curPoint.x) / scaleSize.x,
                                (start.y - curPoint.y) / scaleSize.y
                            )
                            setImageScale(
                                PointF(
                                    originScale.x * rationZoomIn,
                                    originScale.y * rationZoomIn
                                )
                            )
                            setImageTranslation(
                                PointF(
                                    start.x - relativePoint.x * scaleSize.x,
                                    start.y - relativePoint.y * scaleSize.y
                                )
                            )
                        } else {
                            curMode = ZoomMode.Ordinary
                            FitCenter()
                        }
                    } else {
                        lastClickTime = event.getEventTime()
                    }
                }
            }
            MotionEvent.ACTION_POINTER_DOWN ->                 //屏幕上已经有一个点按住 再按下一点时触发该事件
                doubleFingerDistance = getDoubleFingerDistance(event)
            MotionEvent.ACTION_POINTER_UP -> {
                //屏幕上已经有两个点按住 再松开一点时触发该事件
                curMode = ZoomMode.ZoomIn
                scaleDoubleZoom = scaleSize.x / imageSize.x
                if (scaleSize.x < viewSize.x && scaleSize.y < viewSize.y) {
                    curMode = ZoomMode.Ordinary
                    FitCenter()
                }
            }
            MotionEvent.ACTION_MOVE ->                 //手指移动时触发事件
                if (event.pointerCount == 1) {
                    if (curMode == ZoomMode.ZoomIn) {
                        setImageTranslation(PointF(event.getX() - start.x, event.getY() - start.y))
                        start.set(event.getX(), event.getY())
                    }
                } else {
                    //双指缩放时判断是否满足一定距离
                    if (Math.abs(getDoubleFingerDistance(event) - doubleFingerDistance) > 50 && curMode != ZoomMode.DoubleZoomIn) {
                        //获取双指中点
                        center.set(
                            (event.getX(0) + event.getX(1)) / 2,
                            (event.getY(0) + event.getY(1)) / 2
                        )
                        //设置起点
                        start.set(center)
                        curMode = ZoomMode.DoubleZoomIn
                        doubleFingerDistance = getDoubleFingerDistance(event)
                        relativePoint = PointF()

                        //根据图片当前坐标值计算归一化坐标
                        relativePoint.set(
                            (start.x - curPoint.x) / scaleSize.x,
                            (start.y - curPoint.y) / scaleSize.y
                        )
                    }
                    if (curMode == ZoomMode.DoubleZoomIn) {
                        val scale =
                            scaleDoubleZoom * getDoubleFingerDistance(event) / doubleFingerDistance
                        setImageScale(PointF(scale, scale))
                        setImageTranslation(
                            PointF(
                                start.x - relativePoint.x * scaleSize.x,
                                start.y - relativePoint.y * scaleSize.y
                            )
                        )
                    }
                }
            MotionEvent.ACTION_UP -> {
            }
        }

        //注意这里return 的一定要是true 否则只会触发按下事件
        return true
    }


    /**
     * 根据缩放因子缩放图片
     * @param scale
     */
    fun setImageScale(scale: PointF) {
        matrix_t?.setScale(scale.x, scale.y)
        scaleSize.set(scale.x * imageSize.x, scale.y * imageSize.y)
        this.imageMatrix = matrix_t
    }

    /**
     * 根据偏移量改变图片位置
     * @param offset
     */
    fun setImageTranslation(offset: PointF) {
        matrix_t?.postTranslate(offset.x, offset.y)
        curPoint.set(offset)
        this.imageMatrix = matrix_t
    }


    fun getDoubleFingerDistance(event: MotionEvent): Float {
        val x: Float = event.getX(0) - event.getX(1)
        val y: Float = event.getY(0) - event.getY(1)
        return Math.sqrt(x * x + y * y.toDouble()).toFloat()
    }
}