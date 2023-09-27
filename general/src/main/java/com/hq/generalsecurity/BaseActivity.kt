package com.hq.generalsecurity

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.hq.generalsecurity.databinding.ActivityBaseBinding
import com.hq.generalsecurity.expand.*
import com.hq.generalsecurity.formwidget.*
import com.hq.tool.http.FailData
import com.hq.tool.http.Http
import com.hq.tool.http.SuccessData
import com.hq.tool.loge
import com.hq.tool.toast
import okhttp3.Headers

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    var baseBinding: ActivityBaseBinding? = null

    lateinit var viewBinding: T

    val pageData= mutableMapOf<String,Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseBinding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(baseBinding!!.root)
        viewBinding = getBinding()
        baseBinding!!.llActBack.setOnClickListener { v: View? -> onBackPressed() }
        baseBinding!!.imgActMenu.setOnClickListener { v: View? -> onBackPressed() }
        initView()
    }

    protected abstract fun getBinding(): T

    /**
     * 设置标题
     *
     * @param title
     */
    fun setTitle(title: String?) {
        baseBinding!!.txtTitleBaseAct.text = title
    }

    protected abstract fun initView()
    val menu: ImageView
        get() = baseBinding!!.imgActMenu
    val set: TextView
        get() = baseBinding!!.txtSetBaseAct

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v!!.windowToken)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = (left
                    + v.getWidth())
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     * @param token
     */
    protected fun hideSoftInput(token: IBinder?) {
        if (token != null) {
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(
                token,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    /**
     * 解析Text标签
     */
    fun placeText(textSet: TextSet): Parent<*,*> {
        val text = when(textSet.type){
            TextType.Text-> {
                val text= Text()
                text.viewBinding = getViewBinding(text.getViewBindingCls())
                text
            }
            TextType.TextInput-> {
                val text= TextInput()
                text.viewBinding = getViewBinding(text.getViewBindingCls())
                text
            }
            TextType.TextSelect-> {
                val text= TextSelect()
                text.viewBinding = getViewBinding(text.getViewBindingCls())
                text
            }
            TextType.TextCheck-> {
                val text= TextCheck()
                text.viewBinding = getViewBinding(text.getViewBindingCls())
                text
            }
        }

        text.line=textSet

        return text
    }
    /**
     * 解析Img标签
     */
    fun placeImg(imgSet: ImgSet): Parent<*,*> {
        val img= Img()
        img.viewBinding = getViewBinding(img.getViewBindingCls())
        img.line=imgSet

        return  img
    }

    fun http(set:UrlSet?,success:(SuccessData)->Unit,fail:(FailData)->Unit): Unit {
        if(set!=null){
            Http.any(set.requestMethod){
                url= BASE_URL + set.loadUrl
                _headers= Headers.headersOf("Authorization","Bearer ${user?.token}")
                params(getLoadParams(set.loadParams))
                success(success)
                fail(fail)
            }
        }else{
           // fail(FailData("",""))
        }

    }
    private fun getLoadParams(params:ArrayList<LoadParam>): MutableMap<String,Any> {
        val m= mutableMapOf<String,Any>()
        for (p in params){
            when(p.cache){
                CacheType.None-> m.put(p.name,p.def)
                CacheType.Local-> m.put(p.name,getLocalData(p.name))
//                CacheType.Net-> m.put(p.name,getLocalData(p.name))
            }

        }
        return  m
    }

    fun getLocalData(name:String): Any {
        return  ""
    }

    /**
     *
     */
    fun <T: ViewBinding> getViewBinding(cls:Class<T>): T {
        val inflate = cls.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.javaPrimitiveType
        )
        val viewBinding = inflate.invoke(null, layoutInflater, parent, false)
        return viewBinding as T
    }
}