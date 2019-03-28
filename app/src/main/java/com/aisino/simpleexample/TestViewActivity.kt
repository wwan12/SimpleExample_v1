package com.aisino.simpleexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import com.aisino.tool.widget.view.RefreshListView

class TestViewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout: LinearLayout=LinearLayout(this)
        setContentView(layout)
        layout.addView(RefreshListView(this))
    }
}