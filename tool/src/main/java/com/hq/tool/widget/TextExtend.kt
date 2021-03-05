package com.hq.tool.widget

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.text.Html.ImageGetter
import android.util.Log
import android.widget.TextView
import com.hq.tool.bitmap.base64ToBitmap
import java.net.URL
import java.util.*
import kotlin.concurrent.thread

fun TextView.setHtml(text: String): Unit {
    val imageGetter = ImageGetter {
        var drawable: Drawable? = null
        if (it.contains("data:image/png;base64,")) {
            val image = it.substring("data:image/png;base64,".length)
            drawable = BitmapDrawable(it.base64ToBitmap())
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight())
        } else {
            drawable=URLDrawable()
            thread(start = true) {
                val bitmap = BitmapFactory.decodeStream(URL(it).openStream())
                (this.context as Activity).runOnUiThread {
                    (drawable as URLDrawable).bitmapUrl=bitmap
                    (drawable as BitmapDrawable).setBounds(0, 0, bitmap.width, bitmap.height)
                    this.invalidate()
                    this.text = this.text
                  //  drawableLoadText(this,text,imageGetter)
                }
            }
        }
        drawable
    }

    this.text = Html.fromHtml(text, imageGetter, null);
}

private fun drawableLoadText(view: TextView, text: String, getter: ImageGetter){
    view.text = Html.fromHtml(text, getter, null)
}

internal class URLDrawable : BitmapDrawable() {
    var bitmapUrl: Bitmap? = null
    override fun draw(canvas: Canvas) {
        if (bitmapUrl != null) {
            //    drawable.draw(canvas);
            canvas.drawBitmap(bitmapUrl, 0f, 0f, paint)
        } else {
            
        }
    }
}

fun TextView.openTimeSelect(context: Context){
    val ca: Calendar = Calendar.getInstance()
    var mYear: Int = ca.get(Calendar.YEAR)
    var mMonth: Int = ca.get(Calendar.MONTH)
    var mDay: Int = ca.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = DatePickerDialog(context,
            { view, year, month, dayOfMonth ->
                mYear = year
                mMonth = month
                mDay = dayOfMonth
                val sMonth= if ((month + 1)<10){ "0"+(month + 1)  }else{(month + 1).toString()}
                val sDay= if (dayOfMonth<10){ "0$dayOfMonth" }else{dayOfMonth.toString()}
                val data = "${year}-${sMonth}-$sDay"
                this.text = data
            },
            mYear, mMonth, mDay
    )
    datePickerDialog.show()
}