package com.aisino.simpleexample

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aisino.tool.service.showNotifictionIcon
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList
import com.aisino.tool.system.CrashHandler
import com.aisino.tool.http.Http
import com.aisino.tool.model.update.UpdateChecker
import com.aisino.tool.system.getVersionCode


class MainActivity : AppCompatActivity() {

    val exampleList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showNotifictionIcon(0,R.mipmap.ic_launcher,"ceshi","sd","asc")
       example_list.createButtons(this,exampleList,null)
        // 异常处理，不需要处理时注释掉这两句即可！
       CrashHandler.openCrashHandler?.init(applicationContext)
        Http.post{
            url = "接口路径"
            success {   if ((!"version").toInt()>getVersionCode()){
                UpdateChecker.checkForDialog(this@MainActivity,"提示文字","新安装包下载路径")//提示框更新
//                UpdateChecker.checkForNotification(this@MainActivity,"提示文字","新安装包下载路径")//推送更新使用一个就可以
            } }//请求成功
        }

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
