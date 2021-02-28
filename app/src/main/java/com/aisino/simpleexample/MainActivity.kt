package com.aisino.simpleexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList
import com.hq.tool.discreteness.StreamActivity.app
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Base64
import com.hq.tool.system.CrashHandler
import com.hq.tool.system.showNotifictionIcon


class MainActivity : AppCompatActivity() {

    val exampleList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showNotifictionIcon(0,R.mipmap.ic_launcher,"ceshi","sd","asc")
       example_list.createButtons(this,exampleList,null)
        // 异常处理，不需要处理时注释掉这两句即可！
       CrashHandler.openCrashHandler?.init(applicationContext)
        var updateurl=""
        val param = String(Base64.decode("merchantCode=&merchantName=", Base64.DEFAULT))
//        Http.post{
//            url = "https://blog.csdn.net/zybieku/article/details/52925928"
//            success {
//                updateurl=!"cflj"
//                if ((!"version").toInt()>getVersionCode()){
//                    UpdateChecker.checkForDialog(this@MainActivity,"提示文字",updateurl)//提示框更新
////                UpdateChecker.checkForNotification(this@MainActivity,"提示文字","新安装包下载路径")//推送更新使用一个就可以
//            } }//请求成功
//        }
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
