package com.aisino.tool.system

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import java.lang.reflect.Method
import kotlin.math.abs


/***
 * 或者当前activity截图
 */
fun Activity.getWindowBitmap(outMedia:Boolean): Unit {
    val view: View = window.decorView // 获取DecorView
    // 方式一:
//        view.isDrawingCacheEnabled = true
//        view.buildDrawingCache()
//        val bitmap1: Bitmap = view.drawingCache
    // 方式二:
    val bitmap2: Bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas()
    canvas.setBitmap(bitmap2)
    view.draw(canvas)
    if (outMedia){
        // 把文件插入到系统图库
        MediaStore.Images.Media.insertImage(contentResolver, bitmap2, "慧住截图", null)
    }
}

/***
 * 系统截图
 */
fun Activity.systemCrop(): Bitmap? {
    var bmp: Bitmap
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    val dims = floatArrayOf(metrics.widthPixels as Float, metrics.heightPixels as Float)
    val mDisplayMatrix = Matrix()
    // val degrees: Float = getDegreesForRotation(  windowManager.defaultDisplay.rotation)
    val degrees: Float = 0f
    val requiresRotation = degrees > 0
    if (requiresRotation) {
        mDisplayMatrix.reset()
        mDisplayMatrix.preRotate(-degrees)
        mDisplayMatrix.mapPoints(dims)
        dims[0] = abs(dims[0])
        dims[1] = abs(dims[1])
    }
    try {
        val demo = Class.forName("android.view.SurfaceControl")
        val method: Method = demo.getMethod(
                "screenshot",
                *arrayOf<Class<*>>(Integer.TYPE, Integer.TYPE)
        )
        bmp = method.invoke(demo, arrayOf<Any>(Integer.valueOf(dims[0].toInt()), Integer.valueOf(dims[1].toInt()))) as Bitmap
        if (bmp == null) {
            return null
        }
        if (requiresRotation) {
            val ss = Bitmap.createBitmap(
                    metrics.widthPixels,
                    metrics.heightPixels,
                    Bitmap.Config.ARGB_8888
            )
            val c = Canvas(ss)
            c.translate(
                    (ss.width / 2).toFloat(),
                    (ss.height / 2).toFloat()
            )
            c.rotate(degrees)
            c.translate(-dims[0] / 2, -dims[1] / 2)
            c.drawBitmap(bmp, 0f, 0f, null)
            c.setBitmap(null)
            bmp.recycle()
            bmp = ss
        }
        if (bmp == null) {
            return null
        }
        bmp.setHasAlpha(false)
        bmp.prepareToDraw()
        return  bmp
    } catch (e: Exception) {
        e.printStackTrace()
        return  null
    }
}