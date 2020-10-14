package com.aisino.tool.widget.view


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import com.aisino.tool.R

import java.io.InputStream;
import java.lang.reflect.Field;


class GifView : androidx.appcompat.widget.AppCompatImageView{
    /**
     * 是否自动播放
     */
    private var isAutoPlay = false

    /**
     * 播放GIF动画的关键类
     */
    private var mMovie: Movie? = null

    /**
     * gif宽高
     */
    private var bitmapSize: BitmapSize? = null

    /**
     * 播放按钮
     */
 //   private var mStartBotton: Bitmap? = null

    /**
     * 是否正在播放gif
     */
    private var isPlaying = false

    /**
     * gif开始时间
     */
    private var mMovieStart: Long = 0

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        obtainStyledAttr(context, attrs, defStyleAttr)
    }

    private fun obtainStyledAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a: TypedArray = context.theme
            .obtainStyledAttributes(attrs, R.styleable.GifView, defStyleAttr, 0)
        val resId = getIdentifier(a)
        if (resId != 0) {
            // 当资源id不等于0时，就去获取该资源的流
            val `is`: InputStream = resources.openRawResource(resId)
            // 使用Movie类对流进行解码
            mMovie = Movie.decodeStream(`is`)
            //mMovie不等null说明这是一个GIF图片
            if (mMovie != null) {
                this.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                //是否自动播放
                isAutoPlay = a.getBoolean(R.styleable.GifView_auto_play, false)
                /**
                 * 获取gif图片大小
                 */
            //    val bitmap: Bitmap = BitmapFactory.decodeStream(`is`)

                bitmapSize = BitmapSize(mMovie!!.width(), mMovie!!.height())
          //      bitmap.recycle()
                if (!isAutoPlay) {
                    // 当不允许自动播放的时候，得到开始播放按钮的图片，并注册点击事件
                   // mStartBotton = BitmapFactory.decodeResource(getResources(), R.drawable.g1)
                }
            }
        }
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //当时gif图片的时候，控件宽高为gif文件大小
        if (mMovie != null) {
            setMeasuredDimension(bitmapSize!!.width, bitmapSize!!.height)
        }
    }

    override fun onDraw(canvas: Canvas) {
        //当为一张普通的图片的时候
        if (mMovie == null) {
            super.onDraw(canvas)
        } else {
            //如果自动播放的话，就直接播放
            if (isAutoPlay) {
                playMovie(canvas)
                invalidate()
            } else {
                //如果已经点击了播放按钮的话就开始播放gif
                if (isPlaying) {
                    if (playMovie(canvas)) {
                        isPlaying = false
                    }
                    invalidate()
                } else {
                    // 还没开始播放就只绘制GIF图片的第一帧，并绘制一个开始按钮
                    mMovie!!.setTime(0)
                    mMovie!!.draw(canvas, 0f, 0f)
                    val offsetW = bitmapSize!!.width
                    val offsetH = bitmapSize!!.height
                  //  canvas.drawBitmap(mStartBotton!!, offsetW.toFloat(), offsetH.toFloat(), null)
                }
            }
        }
    }

    /**
     * 开始播放GIF动画，播放完成返回true，未完成返回false。
     *
     * @param canvas
     * @return 播放完成返回true，未完成返回false。
     */
    private fun playMovie(canvas: Canvas): Boolean {
        //获取当前时间
        val now: Long = SystemClock.uptimeMillis()
        if (mMovieStart == 0L) {
            mMovieStart = now
        }
        var duration: Int = mMovie!!.duration()
        if (duration == 0) {
            duration = 1000
        }
        val relTime = ((now - mMovieStart) % duration).toInt()
        mMovie!!.setTime(relTime) //不断的设置gif的播放位置
        mMovie!!.draw(canvas, 0f, 0f) //将movie画在canvas上
        //如果（当前时间-gif开始的时间=gif总时长）说明播放完毕了
        if (now - mMovieStart >= duration) {
            mMovieStart = 0
            return true
        }
        return false
    }

    /**
     * 通过反射获取src中的资源id
     * @param a
     */
    private fun getIdentifier(a: TypedArray): Int {
        try {
            val mValueFiled: Field = a.javaClass.getDeclaredField("mValue")
            mValueFiled.isAccessible = true
            val typedValue: TypedValue = mValueFiled.get(a) as TypedValue
            return typedValue.resourceId
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return 0
    }


    public fun Play(){
        isPlaying = true
        invalidate()
    }

    /**
     * BitmapSize
     */
    internal inner class BitmapSize(val width: Int, val height: Int)
}