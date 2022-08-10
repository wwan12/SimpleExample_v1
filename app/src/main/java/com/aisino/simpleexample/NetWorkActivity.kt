package com.aisino.simpleexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.hq.tool.http.Http
import kotlinx.android.synthetic.main.activity_network.*

/**
 * Created by lenovo on 2017/11/30.
 */
class NetWorkActivity : AppCompatActivity() {
    val netWorkList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)
        network_list.createButtons(this, netWorkList, { view: View, type: String -> subHttp(view, type) })

    }

    init {
        netWorkList.add("http_get")
        netWorkList.add("http_post")
        netWorkList.add("http_image")
        netWorkList.add("http_download")
        netWorkList.add("webview_def")
        netWorkList.add("webview_blue")

    }

    fun subHttp(view: View, type: String): Unit {
        view.setOnClickListener {
            var index = 0;
            for (name in netWorkList) {
                if (type == name) {
                    break
                }
                index++
            }
            when (index) {
                0 -> Http.get {
                    url = title_network.text.toString()
                    "kotlin"-"1.6"//添加参数
                    //请求开始
                    start {  }
                    //请求成功
                    success {
//                        it["id"]//取出参数id
//                        it["ids"]//取出集合ids
//                        getEntity<User>("user")//取出实体user
                        !"id"//取出STRING参数id
                        "user".."id"//取出userJOBJ对象中STRING参数id
                    }
                    //请求失败
                    fail { failMsg -> Toast.makeText(this@NetWorkActivity, failMsg.failMsg, Toast.LENGTH_SHORT).show() }
                }
                1 -> Http.post {
                    url ="https://api.weixin.qq.com/card/invoice/seturl?access_token={24_z8k5hT2kD9PSAqClZ3qmpYFioCazGMi2dFOKR8a3xXeIPtEMn-4wTv3KhsaT7p-YC73AI6TRPucFZWJ5iz5C-Bsl1jSvyfGARXXKr1-wUosEQbz3M2U6W2PaxscJXNjAFAUIY}"
                   "{"-"}"
                }
                2 -> Http.upfile {
                    url = title_network.text.toString()
                }
                3 -> Http.download {
                    url = title_network.text.toString()
                }
                4 -> {
//                    FinestWebView().build(this).titleDefault("默认")
//                            .theme(R.style.FinestWebViewTheme_Light_Fullscreen)
//                            .webViewJavaScriptEnabled(true)
//                            .show(title_network.text.toString())
                }
                5 -> {
//                    FinestWebView().build(this).theme(R.style.FinestWebViewTheme)
//                            .titleDefault("Vimeo")
//                            .showUrl(false)
//                            .statusBarColorRes(R.color.bluePrimaryDark)
//                            .toolbarColorRes(R.color.bluePrimary)
//                            .titleColorRes(R.color.finestWhite)
//                            .urlColorRes(R.color.bluePrimaryLight)
//                            .iconDefaultColorRes(R.color.finestWhite)
//                            .progressBarColorRes(R.color.finestWhite)
//                            .stringResCopiedToClipboard(R.string.copied_to_clipboard)
//                            .showSwipeRefreshLayout(true)
//                            .swipeRefreshColorRes(R.color.bluePrimaryDark)
//                            .menuSelector(R.drawable.selector_light_theme)
//                            .menuTextGravity(Gravity.CENTER)
//                            .menuTextPaddingRightRes(R.dimen.defaultMenuTextPaddingLeft)
//                            .dividerHeight(0)
//                            .gradientDivider(false)
//                            .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
//                            .show(title_network.text.toString())
                }
            }

        }
    }
}