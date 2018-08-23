package com.aisino.tool.model.webview

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.webkit.*
import android.webkit.WebView
import android.graphics.Bitmap
import android.graphics.Color
import android.net.http.SslError
import android.view.View
import android.webkit.SslErrorHandler
import android.content.Intent
import android.net.Uri
import android.widget.*
import com.aisino.tool.R
import com.aisino.tool.ani.CircularAnim
import com.aisino.tool.ani.LoadAnim


/**
 * Created by lenovo on 2017/9/14.
 */

class HtmlActivity : AppCompatActivity() {
    private var overTime: Long = 0
    //    https://dqy.he-n-tax.gov.cn:8090/dqy/login_sjd.jsp
//
//        protected val mBaseUrl = "http://75.16.40.202:8080/wdqy/login_sjd.jsp"
    private val protocol = "https://"
    private val mBaseUrl = "dqy.he-n-tax.gov.cn:8090/dqy/"
    private val mStartUrl: String = "login_sjd.jsp"
    private lateinit var webView: WebView
    private lateinit var errorLl:LinearLayout
    private lateinit var errorImg:ImageView
    private lateinit var errorText:TextView
    val NAME = "NAME"
    val imgResList = ArrayList<String>().apply {
//        add("${mBaseUrl}sjd/tzgg/tzggxq.action?")
//        add("${mBaseUrl}sjd/zcsd/zcsdxq.action?")
//        add("${mBaseUrl}sjd/fxts/fxtsxq.action?")
//        add("${mBaseUrl}sjd/Zdsxbg/Zdsxbgxq.action?")
//        add("${mBaseUrl}qyd/sssqs/sssqsck.action?")
    }

    val newLoad = ArrayList<String>().apply {
//        add("hd.chinatax.gov.cn/guoshui/main.jsp")
//        add("12366.bjnsr.gov.cn/")
//        add("12366.chinatax.gov.cn/")
    }

    var needClearHistory = false

    val dialog = CircularAnim()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_html)
        init()
//        bindAlias()
//        Toast.makeText(this, PushManager.getInstance().getClientid(applicationContext) + "_a", Toast.LENGTH_LONG).show()
//        toWebView()

    }


    private fun init() {
        webView= findViewById(R.id.main_web)
        errorLl=findViewById(R.id.error_ll)
        errorImg=findViewById(R.id.error_img)
        errorText=findViewById(R.id.error_text)
        dialog.fullActivity(this,webView)
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT// 不加载缓存
        webView.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.setAppCacheMaxSize(1024 * 1024 * 8)
        webView.settings.setDomStorageEnabled(true);//设置适应HTML5的一些方法
//        val appCachePath = applicationContext.cacheDir.absolutePath
//        main_web.settings.setAppCachePath(appCachePath)
//        main_web.settings.setAppCacheEnabled(true)
        webView.settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        main_web.settings.allowFileAccess = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        } else {
            CookieManager.getInstance().setAcceptCookie(true)
        }

        errorLl.setOnClickListener({
            webView.visibility = View.VISIBLE
            errorLl.visibility = View.GONE
            webView.loadUrl(protocol + mBaseUrl + mStartUrl)
            needClearHistory = true

        })
        //        htmlBinding.mainWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //        htmlBinding.mainWeb.getSettings().setLoadWithOverviewMode(true);

        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                if (message?.indexOf("||") != -1) {
                    getPreferences(Context.MODE_PRIVATE).edit().putString(NAME, message).commit()
                    result?.confirm()
//                    Log.i("bbbb", message)
                    return true
                }
                Toast.makeText(this@HtmlActivity, message, Toast.LENGTH_SHORT).show()
                compatible(message)
                result?.confirm()
                return true
            }
        }

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {//开始加载
                if (url.indexOf("${mBaseUrl}zhuxiaosjd.action") != -1) {//注销
                    needClearHistory = true
                }

                if (url.indexOf("${mBaseUrl}err404.html") == -1) {//404
//                    dialog.setLoadingBuilder(LOAD_TYPE.SINGLE_CIRCLE)
//                            .setLoadingColor(Color.BLUE)//设置图标颜色
//                            .setHintText("请稍等")//设置文本文字
//                            .setHintTextSize(16f) // 设置字体大小
//                            .setHintTextColor(Color.GRAY)  // 设置字体颜色
//                            .show()
                } else {
                    webView.visibility = View.GONE
                    errorLl.visibility = View.VISIBLE
                }
                for (u in newLoad) {//打开外部网站
                    if (url.indexOf(u) != -1) {
                        val intent = Intent()
                        intent.action = "android.intent.action.VIEW"
                        val content_url = Uri.parse(url)
                        intent.data = content_url
                        startActivity(intent)
                        webView.stopLoading()
                    }
                }
//                Log.i("aaaa", "start" + url);
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {//加载完成
                super.onPageFinished(view, url)
                if (needClearHistory) {
                    webView.clearHistory()
                    needClearHistory = false
                    setName()
                }

                if (url.indexOf(mStartUrl) != -1) {
                    setName()
                }

                for (i in imgResList) {
                    if (url.indexOf(i) != -1) {
                        imgReset()
                    }
                }
//                dialog.hide()
//                dialog.cancel()
//                Log.i("aaaa", "finish" + url)

            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {//加载失败
                super.onReceivedError(view, request, error)
//                main_web.loadUrl(mUrl)
                webView.visibility = View.GONE
                errorLl.visibility = View.VISIBLE

            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
//                Log.i("aaaa", "httperror")
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                //      super.onReceivedSslError(view, handler, error);//      一定要注释掉！
                handler.proceed()
            }

//            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
//                super.doUpdateVisitedHistory(view, url, isReload)
//                if (needClearHistory) {
//                    main_web.clearHistory();//清除历史记录
//                }
//            }
        }

        webView.loadUrl(protocol + mBaseUrl + mStartUrl)

    }

    fun setName(): Unit {
        var name = getPreferences(Context.MODE_PRIVATE).getString(NAME, "")
        if (name.length > 2) {
            name = name.substring(2, name.length)
        }
        webView.loadUrl("javascript:(function()\n" +
                " {\n" +
                "    document.getElementById(\"yhmc\").value = \"${name}\";\n" +
                " })()")
    }

    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     */
    private fun imgReset() {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "var tableWidth = window.screen.width; " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];" +
                " var imgWidth = img.width;" +
                "if(imgWidth<tableWidth){ " +
//                "img.style.width = 'auto'; img.style.height = 'auto';" +
                "}else{ " +
                "img.style.maxWidth = '100%'; img.style.height = 'auto';" +
                "} " +
//                "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()")
    }

    fun compatible(msg: String?): Unit {
        when (msg) {
            "用户名或密码错误" -> {
//                dialog.dismiss()
//                dialog.cancel()
            }
        }
    }

//    private fun unBindAlias() {
//        val editText = EditText(this)
//        AlertDialog.Builder(this).setTitle(R.string.unbind_alias).setView(editText)
//                .setPositiveButton(android.R.string.ok) { dialog, which ->
//                    val alias = editText.editableText.toString()
//                    if (alias.length > 0) {
//                        PushManager.getInstance().unBindAlias(this, alias, false)
////                        Log.d(TAG, "unbind alias = " + editText.editableText.toString())
//                    }
//                }.setNegativeButton(android.R.string.cancel, null).show()
//    }

//    private fun bindAlias() {
//        val alias = getPreferences(Context.MODE_PRIVATE).getString(NAME, "")
//            if (alias.length > 0) {
//                PushManager.getInstance().bindAlias(this, alias)
////                            Log.d(TAG, "bind alias = " + editText.editableText.toString())
//            }
//
//    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                //表示按返回键时的操作
                webView.goBack()   //后退
                //webview.goForward();//前进
                return true
            } else {
                canFinish()
            }
        }
        return false
    }

    private fun canFinish() {
        Toast.makeText(this, "再次点击返回关闭应用", Toast.LENGTH_SHORT).show()
        if (overTime == 0L) {
            overTime = System.currentTimeMillis()
            return
        } else {
            if (System.currentTimeMillis() - overTime < 3000) {
                finish()
            } else {
                overTime = 0L
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
//        System.exit(0)

    }
}
