package com.aisino.simpleexample.ani

import android.animation.Animator
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import com.aisino.simpleanimation.load.LOAD_TYPE
import com.aisino.simpleanimation.load.ZLoadingDialog
import com.aisino.simpleanimation.simpview.SimpView
import com.aisino.simpleanimation.simpview.Techniques
import com.aisino.simpleexample.R
import com.aisino.simpleexample.createButtons
import kotlinx.android.synthetic.main.activity_simpview.*

/**
 * Created by lenovo on 2017/11/30.
 */
class SimpViewActivity : AppCompatActivity() {
    val simpviewList = ArrayList<String>()
    var types: Array<Techniques>
    var rope: SimpView.ViewManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simpview)
        ani_simpview_list.createButtons(this, simpviewList, { view: View, type: String -> showAni(view, type) })
        title_simpview_list.text = "测试控件"

    }

    init {
        types = Techniques.values()
        for (t in types){
            simpviewList.add(t.name)
        }
    }

    fun showAni(button: View, name: String): Unit {
        button.setOnClickListener {
            var type: Techniques = types[0]
            for (t in types) {
                if (t.name == name){
                    type = t
                }
            }
            if (rope != null) {
                rope?.stop(true)
            }
            rope = SimpView.with(type)
                    .duration(1200)
                    .repeat(SimpView.INFINITE)
                    .pivot(SimpView.CENTER_PIVOT, SimpView.CENTER_PIVOT)
                    .interpolate(AccelerateDecelerateInterpolator())
                    .withListener(object : Animator.AnimatorListener {//动画状态监听
                        override fun onAnimationStart(animation: Animator) {

                        }

                        override fun onAnimationEnd(animation: Animator) {}

                        override fun onAnimationCancel(animation: Animator) {
                        }

                        override fun onAnimationRepeat(animation: Animator) {

                        }
                    })
                    .playOn(title_simpview_list)
        }
    }
}