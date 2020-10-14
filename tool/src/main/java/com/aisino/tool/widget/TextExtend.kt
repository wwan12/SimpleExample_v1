package com.aisino.tool.widget

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.text.Html.ImageGetter
import android.widget.TextView
import com.aisino.tool.bitmap.base64ToBitmap
import java.net.URL
import kotlin.concurrent.thread

fun TextView.setHtml(text: String): Unit {
    val imageGetter = ImageGetter {
        var drawable: Drawable? = null
        if (it.contains("data:image/png;base64,")) {
            val image = it.substring("data:image/png;base64,".length)
            drawable = BitmapDrawable(it.base64ToBitmap())
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight())
        } else {
            thread(start = true) {
                val bitmap = BitmapFactory.decodeStream(URL(it).openStream())
                (this.context as Activity).runOnUiThread {
                    drawable = BitmapDrawable(bitmap)
                    (drawable as BitmapDrawable).setBounds(0, 0, (drawable as BitmapDrawable).minimumWidth, (drawable as BitmapDrawable).minimumHeight)
                  //  drawableLoadText(this,text,imageGetter)
                }
            }
        }
        drawable
    }

    this.text = Html.fromHtml(text, imageGetter, null);
}

private fun drawableLoadText(view: TextView,text: String,getter:ImageGetter){
    view.text = Html.fromHtml(text,getter,null)
}

