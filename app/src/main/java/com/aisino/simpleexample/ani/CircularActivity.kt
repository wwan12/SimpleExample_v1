package com.aisino.simpleexample.ani

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.aisino.simpleanimation.circular.CircularAnim
import com.aisino.simpleexample.R
import kotlinx.android.synthetic.main.activity_circular.*

/**
 * Created by lenovo on 2017/12/6.
 */
class CircularActivity : AppCompatActivity() {
     var isContentVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circular)
        change_btn.setOnClickListener({
            progressBar.setVisibility(View.VISIBLE)
            // 收缩按钮
            CircularAnim.hide(change_btn).go()
        })

        progressBar.setOnClickListener({
            progressBar.setVisibility(View.GONE)
            // 伸展按钮
            CircularAnim.show(change_btn).go()
        })

        change_btn2.setOnClickListener({
            CircularAnim.hide(change_btn2)
                    .endRadius(progressBar2.getHeight() / 2f)
                    .go({
                        progressBar2.setVisibility(View.VISIBLE)
                        progressBar2.postDelayed(Runnable {
                            CircularAnim.fullActivity(this@CircularActivity, progressBar2)
                                    .go({
                                    })
                        }, 3000)
                    })
        })

        activity_image_btn.setOnClickListener({ view ->
            // 先将图片展出铺满，然后启动新的Activity
            CircularAnim.fullActivity(this@CircularActivity, view)
                    .colorOrImageRes(R.mipmap.simple_text)
                    .go({

                    })
        })

        activity_color_btn.setOnClickListener({ view ->
            // 先将颜色展出铺满，然后启动新的Activity
            CircularAnim.fullActivity(this@CircularActivity, view)
                    .go({
                    })
        })


        logoBtn_iv.setOnClickListener({ view ->
            view.animate().rotationBy(90f)
            // 以 @logoBtn_iv 为中心，收缩或伸展 @mContentLayout
            if (isContentVisible)
                CircularAnim.hide(content_layout).triggerView(logoBtn_iv).go()
            else
                CircularAnim.show(content_layout).triggerView(logoBtn_iv).go()

            isContentVisible = !isContentVisible
        })
    }
}