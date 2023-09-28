package com.aisino.simpleexample.ani

import android.graphics.PointF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import android.widget.Toast
import com.aisino.simpleexample.R
import com.hq.tool.animation.spotlight.CustomTarget
import com.hq.tool.animation.spotlight.SimpleTarget
import com.hq.tool.animation.spotlight.Spotlight
import kotlinx.android.synthetic.main.activity_spotlight.*

/**
 * Created by lenovo on 2017/12/6.
 */
class SpotlightActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotlight)

        simple_target.setOnClickListener {
            val oneLocation = IntArray(2)
            one.getLocationInWindow(oneLocation)
            val oneX = oneLocation[0] + one.width / 2f
            val oneY = oneLocation[1] + one.height / 2f
            // make an target
            val firstTarget = SimpleTarget.Builder(this@SpotlightActivity).setPoint(oneX, oneY)
                .setRadius(100f)
                .setTitle("title 1")
                .setDescription("description 1")
                .build()

            val twoLocation = IntArray(2)
            two.getLocationInWindow(twoLocation)
            val point =
                PointF(twoLocation[0] + two.getWidth() / 2f, twoLocation[1] + two.getHeight() / 2f)
            // make an target
            val secondTarget = SimpleTarget.Builder(this@SpotlightActivity).setPoint(point)
                .setRadius(80f)
                .setTitle("title 2")
                .setDescription("description 2")
                .build()

            val thirdTarget = SimpleTarget.Builder(this@SpotlightActivity).setPoint(three)
                .setRadius(200f)
                .setTitle("title 3")
                .setDescription("description 3")
                .build()

            Spotlight.with(this@SpotlightActivity)
                .setDuration(1000L)
                .setAnimation(DecelerateInterpolator(2f))
                .setTargets(firstTarget, secondTarget, thirdTarget)
                .setOnSpotlightStartedListener {
                    Toast.makeText(this@SpotlightActivity, "开始", Toast.LENGTH_SHORT).show()
                }
                .setOnSpotlightEndedListener {
                    Toast.makeText(this@SpotlightActivity, "结束", Toast.LENGTH_SHORT).show()
                }
                .start()
        }

        custom_target.setOnClickListener {
            // make an target
            val thirdTarget = CustomTarget.Builder(this@SpotlightActivity).setPoint(three)
                .setRadius(200f)
                .setView(TextView(this).apply {
                    text = "点击这里"
//                        setTextColor(resources.getColor(R.color.))
                    textSize = 20f
                })
                .build()

            Spotlight.with(this@SpotlightActivity)
                .setDuration(800L)
                .setAnimation(DecelerateInterpolator(2f))
                .setTargets(thirdTarget)
                .setOnSpotlightStartedListener {
                    Toast.makeText(this@SpotlightActivity, "开始spotlight", Toast.LENGTH_SHORT)
                        .show()
                }
                .setOnSpotlightEndedListener {
                    Toast.makeText(this@SpotlightActivity, "结束spotlight", Toast.LENGTH_SHORT).show()
                }
                .start()
        }
    }
}