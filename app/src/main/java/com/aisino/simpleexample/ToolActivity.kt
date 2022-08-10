package com.aisino.simpleexample

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.hq.tool.cache.ACache
import com.hq.tool.log
import com.hq.tool.model.webview.HtmlActivity
import com.hq.tool.system.*
import com.hq.tool.toast
import kotlinx.android.synthetic.main.activity_tool.*

/**
 * Created by lenovo on 2017/12/4.
 */
class ToolActivity : AppCompatActivity() {
    val tools: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tool)
        tool_list.createButtons(this, tools, { view: View, type: String -> openTool(view, type) })
    }

    init {
        tools = ArrayList()
        tools.add("Cache")
        tools.add("Permissions")
        tools.add("Camera")
        tools.add("Gallery")
        tools.add("List")
        tools.add("WebView")
    }

    fun openTool(view: View, type: String): Unit {
        view.setOnClickListener {
            select(type)
        }
    }

    fun select(type: String): Unit {
        tool_attach.removeAllViews()
        when (type) {
            tools[0] -> cache()
            tools[1] -> permissions()
            tools[2] -> camera()
            tools[3] -> gallery()
            tools[4] -> list()
            tools[5] -> webview()
        }
    }

    fun cache(): Unit {
        val key = EditText(this)
        key.hint = "储存参数KEY"
        tool_attach.addView(key)
        val value = EditText(this)
        value.hint = "储存参数VALUE"
        tool_attach.addView(value)
        val cache = AppCompatButton(this).apply {
            text = "储存"
            setOnClickListener {

                    ACache.get(this@ToolActivity).put(key.text.toString(), value.text.toString())
            }
        }
        val get = AppCompatButton(this).apply {
            text = "读取"
            setOnClickListener {
                val valueText=ACache.get(this@ToolActivity).getAsString(key.text.toString())
                    valueText?.toast(this@ToolActivity)
                (valueText+"}}}").log()
                value.setText(valueText)
            }
        }
        tool_attach.addView(cache)
        tool_attach.addView(get)
    }

    fun permissions(): Unit {
        signPermissions()
    }

    var actReust = { requestCode: Int, resultCode: Int, data: Intent? -> }
    fun camera(): Unit {
        val showImg = ImageView(this)
        val uri = openCamera()
        tool_attach.addView(showImg)
        actReust = { requestCode, resultCode, data ->
            if (requestCode == CAMERA_REQUEST) {
                showImg.setImageBitmap(uri.getCameraImg(this))
            }
        }
    }


    fun gallery(): Unit {
        val showImg = ImageView(this)
        tool_attach.addView(showImg)
        openGallery()
        actReust = { requestCode, resultCode, data ->
            if (requestCode == GALLERY_REQUEST) {
                if (data?.getData() != null) {
                    showImg.setImageBitmap(data.data?.handleImageOnKitKat(this))
                }
            }
        }
    }

    fun list(): Unit {
        startActivity(Intent(this,TestViewActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        actReust(requestCode, resultCode, data)
    }
    fun webview(): Unit {
        startActivity(Intent(this,HtmlActivity::class.java))
    }
}