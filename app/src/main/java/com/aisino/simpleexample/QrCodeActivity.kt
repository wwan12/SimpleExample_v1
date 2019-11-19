package com.aisino.simpleexample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aisino.qrcode.activity.CaptureActivity

/**
 * Created by lenovo on 2017/12/13.
 */
class QrCodeActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this,CaptureActivity::class.java))
    }
}