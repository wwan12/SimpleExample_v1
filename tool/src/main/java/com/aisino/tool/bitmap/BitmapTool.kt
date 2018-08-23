package com.aisino.tool.bitmap

/**
 * Created by lenovo on 2017/12/5.
 */

import android.app.Activity
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.LruCache

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

/**
 * Android根据设备屏幕尺寸和dpi的不同，给系统分配的单应用程序内存大小也不同，具体如下表
 *
 * 屏幕尺寸 DPI 应用内存
 * small / normal / large ldpi / mdpi 16MB
 * small / normal / large tvdpi / hdpi 32MB
 * small / normal / large xhdpi 64MB
 * small / normal / large 400dpi 96MB
 * small / normal / large xxhdpi 128MB
 * -------------------------------------------------------
 * xlarge mdpi 32MB
 * xlarge tvdpi / hdpi 64MB
 * xlarge xhdpi 128MB
 * xlarge 400dpi 192MB
 * xlarge xxhdpi 256MB
 */

/**
 * 图片加载及转化工具 ----------------------------------------------------------------------- 延伸：一个Bitmap到底占用多大内存？系统给每个应用程序分配多大内存？ Bitmap占用的内存为：像素总数
 * * 每个像素占用的内存。在Android中， Bitmap有四种像素类型：ARGB_8888、ARGB_4444、ARGB_565、ALPHA_8， 他们每个像素占用的字节数分别为4、2、2、1。因此，一个2000*1000的ARGB_8888
 * 类型的Bitmap占用的内存为2000*1000*4=8000000B=8MB。
 *
 *
 */
/**
 * 初始化lrucache,最少使用最先移除,LruCache来缓存图片，
 * 当存储Image的大小大于LruCache设定的值，系统自动释放内存，
 */
private var mMemoryCache: LruCache<String, Bitmap>? = null

fun Activity.initLru() {
    val memory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    val cacheSize = memory / 8
    mMemoryCache = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            // return value.getByteCount() / 1024;
            return value.height * value.rowBytes
        }
    }
}

// ---lrucache----------------------------------------------------
/**
 * 添加图片到lrucache
 *
 * @param key
 * @param bitmap
 */
@Synchronized
fun Activity.addBitmapToMemCache(key: String?, bitmap: Bitmap?) {
    if (getBitmapFromMemCache(key) == null) {
        if ((key != null) and (bitmap != null)) {
            mMemoryCache!!.put(key, bitmap)
        }
    }
}

/**
 * 清除缓存
 */
fun Activity.clearMemCache() {
    if (mMemoryCache != null) {
        if (mMemoryCache!!.size() > 0) {
            mMemoryCache!!.evictAll()
        }
        mMemoryCache = null
    }
}

/**
 * 移除缓存
 */
@Synchronized
fun Activity.removeMemCache(key: String?) {
    if (key != null) {
        if (mMemoryCache != null) {
            val bm = mMemoryCache!!.remove(key)
            bm?.recycle()
        }
    }
}

/**
 * 从lrucache里读取图片
 * @param key
 * @return
 */
fun Activity.getBitmapFromMemCache(key: String?): Bitmap? {
    return if (key != null) {
        mMemoryCache!!.get(key)
    } else null
}

/**
 * 从网上下载图片
 *
 * @param imageUrl
 * @return
 */
fun Activity.downloadBitmap(imageUrl: String): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        bitmap = BitmapFactory.decodeStream(URL(imageUrl).openStream())
        return bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}

/**
 * drawable 转bitmap
 *
 * @param drawable
 * @return
 */
fun Drawable.drawable2Bitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight,
            if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
    val canvas = Canvas(bitmap)
    // canvas.setBitmap(bitmap);
    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    draw(canvas)
    return bitmap
}

/**
 * bitmap 转 drawable
 *
 * @param bm
 * @return
 */
fun Bitmap.bitmap2Drable(): Drawable {
    return BitmapDrawable(this)
}


/**
 * 把图片转换成字节数组
 * @return
 */
fun Bitmap.bitmap2Byte(): ByteArray? {
    val outBitmap = Bitmap.createScaledBitmap(this, 150, this.height * 150 / this.width, true)
    if (this != outBitmap) {
        this.recycle()
    }
    var compressData: ByteArray? = null
    val baos = ByteArrayOutputStream()
    try {
        try {
            outBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        compressData = baos.toByteArray()
        baos.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return compressData
}

/**
 * 缩放图片
 *
 * @param bitmap
 * 原图片
 * @param newWidth
 * @param newHeight
 * @return
 */
fun Bitmap.setBitmapSize( newWidth: Int, newHeight: Int): Bitmap {
    val width = this.width
    val height = this.height
    val scaleWidth = newWidth * 1.0f / width
    val scaleHeight = newHeight * 1.0f / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

/**
 * 计算图片的缩放大小 如果==1，表示没变化，==2，表示宽高都缩小一倍 ----------------------------------------------------------------------------
 * inSampleSize是BitmapFactory.Options类的一个参数，该参数为int型， 他的值指示了在解析图片为Bitmap时在长宽两个方向上像素缩小的倍数。inSampleSize的默认值和最小值为1（当小于1时，解码器将该值当做1来处理），
 * 且在大于1时，该值只能为2的幂（当不为2的幂时，解码器会取与该值最接近的2的幂）。 例如，当inSampleSize为2时，一个2000*1000的图片，将被缩小为1000*500，相应地， 它的像素数和内存占用都被缩小为了原来的1/4：
 *
 * @param options
 * @param reqWidth
 * @param reqHeight
 * @return
 */
private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // 原始图片的宽高
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2
        // 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
        while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

/**
 * 根据计算出的inSampleSize生成Bitmap(此时的bitmap是经过缩放的图片)
 *
 * @param res
 * @param resId
 * @param reqWidth
 * @param reqHeight
 * @return
 */
fun Activity.decodeSampledBitmapFromResource(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
    // 首先设置 inJustDecodeBounds=true 来获取图片尺寸
    val options = BitmapFactory.Options()
    /**
     * inJustDecodeBounds属性设置为true，decodeResource()方法就不会生成Bitmap对象，而仅仅是读取该图片的尺寸和类型信息：
     */
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, resId, options)

    // 计算 inSampleSize 的值
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    // 根据计算出的 inSampleSize 来解码图片生成Bitmap
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, resId, options)
}

/**
 * 将图片保存到本地时进行压缩, 即将图片从Bitmap形式变为File形式时进行压缩,
 * 特点是: File形式的图片确实被压缩了, 但是当你重新读取压缩后的file为 Bitmap是,它占用的内存并没有改变
 *
 * @param bmp
 * @param file
 */
fun Bitmap.compressBmpToFile(file: File) {
    val baos = ByteArrayOutputStream()
    var options = 80// 个人喜欢从80开始,
    this.compress(Bitmap.CompressFormat.JPEG, options, baos)
    while (baos.toByteArray().size / 1024 > 100) {
        baos.reset()
        options -= 10
        this.compress(Bitmap.CompressFormat.JPEG, options, baos)
    }
    try {
        val fos = FileOutputStream(file)
        fos.write(baos.toByteArray())
        fos.flush()
        fos.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * 将图片从本地读到内存时,进行压缩 ,即图片从File形式变为Bitmap形式
 * 特点: 通过设置采样率, 减少图片的像素, 达到对内存中的Bitmap进行压缩
 * @param srcPath
 * @return
 */
fun Activity.compressImageFromFile(srcPath: String, pixWidth: Float, pixHeight: Float): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true// 只读边,不读内容
    var bitmap:Bitmap

    options.inJustDecodeBounds = false
    val w = options.outWidth
    val h = options.outHeight
    var scale = 1
    if (w > h && w > pixWidth) {
        scale = (options.outWidth / pixWidth).toInt()
    } else if (w < h && h > pixHeight) {
        scale = (options.outHeight / pixHeight).toInt()
    }
    if (scale <= 0)
        scale = 1
    options.inSampleSize = scale// 设置采样率

    options.inPreferredConfig = Bitmap.Config.ARGB_8888// 该模式是默认的,可不设
    options.inPurgeable = true// 同时设置才会有效
    options.inInputShareable = true// 。当系统内存不够时候图片自动被回收

    bitmap = BitmapFactory.decodeFile(srcPath, options)
    // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
    // 其实是无效的,大家尽管尝试
    return bitmap
}

/**将图片改为圆角类型
 * @param bitmap
 * @param pixels
 * @return
 */
fun Bitmap.toRoundCorner(pixels: Int): Bitmap {
    val output = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val color = -0xbdbdbe
    val paint = Paint()
    val rect = Rect(0, 0, this.width, this.height)
    val rectF = RectF(rect)
    val roundPx = pixels.toFloat()
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = color
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)
    return output
}

