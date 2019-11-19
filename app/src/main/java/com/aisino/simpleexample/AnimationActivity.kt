package com.aisino.simpleexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_animation.*

/**
 * Created by lenovo on 2017/11/30.
 */
class AnimationActivity: AppCompatActivity(){
    val aniList=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        ani_list.createButtons(this,aniList,null)
    }

    init {
        aniList.add("ani.Load")
        aniList.add("ani.SimpView")
        aniList.add("ani.Circular")
        aniList.add("ani.Spotlight")

    }


}