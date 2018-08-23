package com.aisino.simpleexample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aisino.tool.service.showNotifictionIcon
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList
import com.aisino.tool.system.CrashHandler




class MainActivity : AppCompatActivity() {

    val exampleList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method

//        Toast.showToast_w(this,"aaaaa")
        showNotifictionIcon(0,R.mipmap.ic_launcher,"ceshi","sd","asc")
       example_list.createButtons(this,exampleList,null)
        // 异常处理，不需要处理时注释掉这两句即可！
       CrashHandler.openCrashHandler?.init(applicationContext)

    }

    init {
        exampleList.add("Animation")
        exampleList.add("NetWork")
        exampleList.add("Tool")
        exampleList.add("danmuku")
        exampleList.add("IndependentModule")
        exampleList.add("QrCode")
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
