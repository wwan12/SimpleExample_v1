package com.aisino.tool.widget

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.aisino.tool.R
import com.aisino.tool.system.dip2px
import com.aisino.tool.system.getScreenWidth


/**
 * 自定义 Toast
 * 不重复显示
 */
object ToastUp {


    private var mToast: android.widget.Toast? = null

    private val mHandler = Handler()
    private val r = Runnable {
        mToast?.cancel()
        mToast = null//toast隐藏后，将其置为null
    }

    /**
     * 警告
     * 显示自定义的土司
     * @param context 上下文
     * @param text 显示的文本
     */
    fun showToast_w(context: Context, text: String) {//,int iconid
        val view = View.inflate(context, R.layout.toast_warning, null)

        val ll = view.findViewById<View>(R.id.ll_toast_warning) as LinearLayout
        val params = ll.layoutParams as LinearLayout.LayoutParams
        params.width = (context as Activity).getScreenWidth()
        params.height = (context as Activity).dip2px(50f)
        ll.layoutParams = params
        val tv = view.findViewById<View>(R.id.tv_my_toast_warning) as TextView
        val iv = view.findViewById<View>(R.id.iv_my_toast_warning) as ImageView
        //        iv.setImageResource(iconid);
        iv.setImageResource(R.mipmap.icon_toast_warning)
        tv.text = text
        mHandler.removeCallbacks(r)
        if (mToast == null) {//只有mToast==null时才重新创建，否则只需更改提示文字
            mToast = android.widget.Toast(context)
            mToast?.duration = android.widget.Toast.LENGTH_SHORT
            mToast?.setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)
            mToast?.view = view
        }
        mHandler.postDelayed(r, 1000)//延迟1秒隐藏toast
        mToast?.show()

        //        Toast toast = new Toast(context);
        ////        toast.setGravity(Gravity.TOP, 0, 0);
        //        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.TOP, 0, 0);
        //        toast.setDuration(Toast.LENGTH_SHORT);
        //        toast.setView(view);
        //        toast.show();
    }

    /**
     * 错误
     * 显示自定义的土司
     * @param context 上下文
     * @param text 显示的文本
     */
    fun showToast_e(context: Context, text: String) {//,int iconid
        val view = View.inflate(context, R.layout.toast_error, null)

        val ll = view.findViewById<View>(R.id.ll_toast_error) as LinearLayout
        val params = ll.layoutParams as LinearLayout.LayoutParams
        params.width = (context as Activity).getScreenWidth()
        params.height = (context as Activity).dip2px(50f)
        ll.layoutParams = params
        val tv = view.findViewById<View>(R.id.tv_my_toast_error) as TextView
        val iv = view.findViewById<View>(R.id.iv_my_toast_error) as ImageView
        //        iv.setImageResource(iconid);
        iv.setImageResource(R.mipmap.icon_toast_error)
        tv.text = text
        mHandler.removeCallbacks(r)
        if (mToast == null) {//只有mToast==null时才重新创建，否则只需更改提示文字
            mToast = android.widget.Toast(context)
            mToast?.duration = android.widget.Toast.LENGTH_SHORT
            mToast?.setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)
            mToast?.view = view
        }
        mHandler.postDelayed(r, 1000)//延迟1秒隐藏toast
        mToast?.show()
        //        Toast toast = new Toast(context);
        ////        toast.setGravity(Gravity.TOP, 0, 0);
        //        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.TOP, 0, 0);
        //        toast.setDuration(Toast.LENGTH_SHORT);
        //        toast.setView(view);
        //        toast.show();
    }

    /**
     * 正确
     * 显示自定义的土司
     * @param context 上下文
     * @param text 显示的文本
     */
    fun showToast_r(context: Context, text: String) {//,int iconid
        val view = View.inflate(context, R.layout.toast_right, null)

        val ll = view.findViewById<View>(R.id.ll_toast_right) as LinearLayout
        val params = ll.layoutParams as LinearLayout.LayoutParams
        params.width = (context as Activity).getScreenWidth()
        params.height = (context as Activity).dip2px(50f)
        ll.layoutParams = params
        val tv = view.findViewById<View>(R.id.tv_my_toast_right) as TextView
        val iv = view.findViewById<View>(R.id.iv_my_toast_right) as ImageView
        //        iv.setImageResource(iconid);
        iv.setImageResource(R.mipmap.icon_toast_right)
        tv.text = text
        mHandler.removeCallbacks(r)
        if (mToast == null) {//只有mToast==null时才重新创建，否则只需更改提示文字
            mToast = android.widget.Toast(context)
            mToast?.duration = android.widget.Toast.LENGTH_SHORT
            mToast?.setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)
            mToast?.view = view
        }
        mHandler.postDelayed(r, 1000)//延迟1秒隐藏toast
        mToast?.show()
        //        Toast toast = new Toast(context);
        ////        toast.setGravity(Gravity.TOP, 0, 0);
        //        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.TOP, 0, 0);
        //        toast.setDuration(Toast.LENGTH_SHORT);
        //        toast.setView(view);
        //        toast.show();
    }

}
