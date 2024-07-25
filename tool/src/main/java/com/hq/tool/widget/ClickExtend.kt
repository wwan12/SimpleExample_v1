package com.hq.tool.widget

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.hq.tool.animation.LoadingDialog

private val controllerViews= mutableMapOf<Int,Long>()

private val MIN_DELAY_TIME=1000



fun View.setOnNotFastClick(click:(view: View)->Unit){
    this.setOnClickListener {
        if (controllerViews.containsKey(this.id)){
            var flag = true
            val currentClickTime = System.currentTimeMillis()
            if (currentClickTime - controllerViews[this.id]!! >= MIN_DELAY_TIME) {
                flag = false
                controllerViews.put(this.id,currentClickTime)
            }
            if (!flag){
                click(this)
            }
        }else{
            controllerViews.put(this.id,System.currentTimeMillis())
            click(this)
        }

    }
}

fun View.setOnChangeView(views:List<View>  ): Unit {

}