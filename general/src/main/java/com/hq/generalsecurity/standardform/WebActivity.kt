package com.hq.generalsecurity.standardform

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.hq.generalsecurity.BaseActivity
import com.hq.generalsecurity.databinding.ActivityWebBinding
import com.hq.tool.animation.CircularAnim
import com.hq.tool.animation.LoadAnim
import com.hq.tool.system.CAMERA_REQUEST
import com.hq.tool.system.openCamera
import com.hq.tool.toast

class WebActivity:BaseActivity<ActivityWebBinding>() {

    private var overTime: Long = 0
    //    private val protocol = "https://"
    private var mBaseUrl = ""
    private var errorUrl: String? = null
    private var isSingle=false
    private var isAutoStyle=false
    private var uploadMessageAboveL: ValueCallback<Array<Uri>>?=null
    private var cUri: Uri?=null
    private var loadLog: AlertDialog?=null

    private val jsChangeStyle = "javascript:function myFunction(){\n" +
            "var \$jquery = jQuery.noConflict();\n" +
            "var content=\$jquery('.article');\n" +
            "\$jquery('body').empty();\n" +
            "content.css({\n" +
            "background:\"#fff\",\n" +
            "position:\"absolute\",\n" +
            "top:\"0\",left:\"0\",\n" +
            "});\n" +
            "\$jquery('body').append(content);\n" +
            "\$jquery(\"div\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
            "\$jquery(\"a\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
            "\$jquery(\"h1\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
            "\$jquery(\"h2\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
            "\$jquery(\"h3\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
            "\$jquery(\"h4\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
            "\$jquery(\"h5\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
            "\$jquery(\"h6\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
            "\$jquery(\"img\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
            "\$jquery(\"img\").css({width: \"100%\",height:\"100%\",objecFit:\"cover\"});\n" +
            "\$jquery(\"h1\").css({paddingBottom: \"0.3em\",fontSize:\"2em\",borderBottom:\"1px solid #eaecef\"});\n" +
            "\$jquery(\"h2\").css({paddingBottom: \"0.3em\",fontSize:\"1.5em\",borderBottom:\"1px solid #eaecef\"});\n" +
            "\$jquery(\"h3\").css({fontSize:\"1.25em\"});\n" +
            "\$jquery(\"h4\").css({fontSize:\"1em\"});\n" +
            "\$jquery(\"h5\").css({fontSize:\"0.875em\"});\n" +
            "\$jquery(\"h6\").css({fontSize:\"0.85em\"});\n}"

    val newLoad = ArrayList<String>().apply {

    }

    var needClearHistory = false

    val dialog = CircularAnim()
    
    override fun getBinding(): ActivityWebBinding {
        return  ActivityWebBinding.inflate(layoutInflater,null,false)
    }

    override fun initView() {
        init()
    }
    
    private fun init() {
        dialog.fullActivity(this, viewBinding.webView)

        viewBinding.webView.settings.defaultFontSize = 22
        viewBinding.webView.settings.minimumFontSize = 16//设置 WebView 支持的最小字体大小，默认为 8
        viewBinding.webView.settings.cacheMode = WebSettings.LOAD_DEFAULT// 不加载缓存
        viewBinding.webView.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        viewBinding.webView.settings.javaScriptEnabled = true
        viewBinding.webView.settings.domStorageEnabled = true
        viewBinding.webView.settings.domStorageEnabled = true;//设置适应HTML5的一些方法
        viewBinding.webView.settings.blockNetworkImage = false//是否阻塞加载网络图片  协议http or https
        viewBinding.webView.settings.allowFileAccess = true //允许加载本地文件html  file协议, 这可能会造成不安全 , 建议重写关闭
        viewBinding.webView.settings.allowFileAccessFromFileURLs = false //通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
        viewBinding.webView.settings.allowUniversalAccessFromFileURLs = false//允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
        viewBinding.webView.settings.textZoom=100

        viewBinding.webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS;
//        main_web.settings.allowFileAccess = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(viewBinding.webView, true)
        } else {
            CookieManager.getInstance().setAcceptCookie(true)
        }

        viewBinding.webView.addJavascriptInterface(object : Object(){
            @JavascriptInterface
            fun jsAndroid(msg : String){

            }
            //第二个参数可以自己随便设置，在html里会用到
        },"androids")

        viewBinding.errorLl.setOnClickListener {
            viewBinding.webView.visibility = View.VISIBLE
            viewBinding.errorLl.visibility = View.GONE
            viewBinding.webView.loadUrl(mBaseUrl)
            needClearHistory = true

        }

        viewBinding.webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                message?.toast(this@WebActivity)
                result?.confirm()
                return true
            }

            var isNeedExe = true
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress > 95 && isNeedExe&&isAutoStyle) {
                    isNeedExe = !isNeedExe
                    viewBinding.webView.loadUrl(jsChangeStyle)
                    viewBinding.webView.loadUrl("javascript:myFunction()")
                }
                super.onProgressChanged(view, newProgress)

            }

            override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
                uploadMessageAboveL = filePathCallback
                //调用系统相机或者相册
                cUri= openCamera()
                return true
            }

            override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
                callback?.invoke(origin, true, false)
                super.onGeolocationPermissionsShowPrompt(origin, callback)
            }

        }

        viewBinding.webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {//开始加载
                for (u in newLoad) {//打开外部网站
                    if (url.indexOf(u) != -1) {
                        val intent = Intent()
                        intent.action = "android.intent.action.VIEW"
                        val content_url = Uri.parse(url)
                        intent.data = content_url
                        startActivity(intent)
                        viewBinding.webView.stopLoading()
                    }
                }
                showLoad()
                super.onPageStarted(view, url, favicon)
            }



            override fun onPageFinished(view: WebView, url: String) {//加载完成
                super.onPageFinished(view, url)
                if (needClearHistory) {
                    viewBinding.webView.clearHistory()
                    needClearHistory = false
//                    setName()
                }
                imgReset()
                hideLoad()
            
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {//加载失败
                super.onReceivedError(view, request, error)
//                main_web.loadUrl(mUrl)
                viewBinding.webView.visibility = View.GONE
                viewBinding.errorLl.visibility = View.VISIBLE

            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                //      super.onReceivedSslError(view, handler, error);//      一定要注释掉！
                handler.proceed()
            }
        }

        viewBinding.webView.loadUrl(mBaseUrl)

    }

//    fun setName(): Unit {
//        var name = getPreferences(Context.MODE_PRIVATE).getString(NAME, "")
//        if (name.length > 2) {
//            name = name.substring(2, name.length)
//        }
//        viewBinding.webView.loadUrl("javascript:(function()\n" +
//                " {\n" +
//                "    document.getElementById(\"yhmc\").value = \"${name}\";\n" +
//                " })()")
//    }

    fun setCook(key:String,v:String): Unit {
        viewBinding.webView.loadUrl("javascript:(function()\n" +
                " {\n" +
                "      document.cookie=\"${key}=\"+\"${v}\";\n" +
                " })()")
    }

    fun setTextSize(key:String,v:String): Unit {
        viewBinding.webView.loadUrl("javascript:(function htmlFontSize(){\n" +
                "    var h = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);\n" +
                "    var w = Math.max(document.documentElement.clientWidth, window.innerWidth || 0);\n" +
                "    var width = w > h ? h : w;\n" +
                "    width = width > 720 ? 720 : width\n" +
                "    var fz = ~~(width*100000/36)/10000\n" +
                "    document.getElementsByTagName(\"html\")[0].style.cssText = 'font-size: ' + fz +\"px\";\n" +
                "    var realfz = ~~(+window.getComputedStyle(document.getElementsByTagName(\"html\")[0]).fontSize.replace('px','')*10000)/10000\n" +
                "    if (fz !== realfz) {\n" +
                "        document.getElementsByTagName(\"html\")[0].style.cssText = 'font-size: ' + fz * (fz / realfz) +\"px\";\n" +
                "    }\n" +
                "})()")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ( uploadMessageAboveL == null) {
            return
        }
        //取消拍照或者图片选择时,返回null,否则<input file> 就是没有反应
        if (resultCode != RESULT_OK) {
            if (uploadMessageAboveL != null) {
                uploadMessageAboveL?.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
        }
        //拍照成功和选取照片时
        if (requestCode== CAMERA_REQUEST) {
            if (uploadMessageAboveL != null) {
                uploadMessageAboveL?.onReceiveValue(arrayOf<Uri>(cUri!!))
                uploadMessageAboveL = null;
            }
        }
    }

    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     */
    private fun imgReset() {
        viewBinding.webView.loadUrl("javascript:(function(){" +
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

    override fun onBackPressed() {
        super.onBackPressed()
        if (isSingle){
            if ( viewBinding.webView.canGoBack()) {
                //表示按返回键时的操作
                viewBinding.webView.goBack()   //后退
                //viewBinding.webView.goForward();//前进
            } else {
                canFinish()
            }
        }
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


    protected fun showLoad(): Unit {
        val load = AlertDialog.Builder(this)
        load.setView(LoadAnim(this))
        loadLog = load.show()
    }

    protected fun hideLoad(): Unit {
        if (loadLog != null && loadLog?.isShowing!!) {
            loadLog?.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.webView.destroy()
    }
}