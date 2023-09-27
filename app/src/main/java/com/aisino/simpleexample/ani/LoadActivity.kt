package com.aisino.simpleexample.ani

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View

import com.aisino.simpleexample.R
import com.aisino.simpleexample.createButtons
import com.hq.tool.animation.load.LOAD_TYPE
import com.hq.tool.animation.load.ZLoadingDialog
import kotlinx.android.synthetic.main.activity_load.*

/**
 * Created by lenovo on 2017/11/30.
 */
class LoadActivity : AppCompatActivity() {
    val loadList = ArrayList<String>()
    var types: Array<LOAD_TYPE>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)
        ani_load_list.createButtons(this, loadList, { view: View, type: String -> showAni(view, type) })
    }

    init {
        types = LOAD_TYPE.values()
        for (t in types) {
            loadList.add(t.name)
        }
    }

    fun showAni(button: View, name: String): Unit {
        button.setOnClickListener {
            var type: LOAD_TYPE = types[0]
            for (t in types) {
                if (t.name == name){
                    type = t
                }
            }
            val dialog = ZLoadingDialog(this@LoadActivity)
            dialog.setLoadingBuilder(type)
                    .setLoadingColor(Color.BLUE)//设置图标颜色
                    .setHintText("Loading...")//设置文本文字
                    .setHintTextSize(16f) // 设置字体大小
                    .setHintTextColor(Color.GRAY)  // 设置字体颜色
                    .show()
        }
    }
}