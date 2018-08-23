package com.aisino.simpleexample

import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatButton
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * Created by lenovo on 2017/11/30.
 */
fun LinearLayout.createButtons(context: Context, list: ArrayList<String>, function: ((view: View, type: String) -> Unit)?) {
    for (type in list) {
        val button = AppCompatButton(context).apply {
            val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
            setPadding(padding, padding, padding, padding)
            text = type
        }
        this.addView(button, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        if (function==null){
            setupListener(context,button,type)
        }else{
            function(button,type)
        }
    }

}

fun setupListener(context: Context, view: View, type: String) {
    view.setOnClickListener {
        val AName="com.aisino.simpleexample."+type+"Activity"
        val send = Class.forName(AName)
        val intent= Intent(context,send)
        context.startActivity(intent)
    }
}