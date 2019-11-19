package com.aisino.simpleexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout

class TestViewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout: LinearLayout=LinearLayout(this)
        setContentView(layout)
     //   layout.addView(RefreshListView(this))
    }
}