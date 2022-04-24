package com.hq.tool.system

import android.app.Activity
import android.content.pm.PackageManager
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.app.ActivityCompat
import com.hq.tool.R

//fun Activity.openCallAndCopyWindow(text:String) {
//    if(PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)){
//        // 将布局文件转换成View对象，popupview 内容视图
//        val mPopView = this.layoutInflater.inflate(R.layout.call_copy_window, null)
//        // 将转换的View放置到 新建一个popuwindow对象中
//        val mPopupWindow = PopupWindow(mPopView,
//            this.windowManager.defaultDisplay.width,
//            LinearLayout.LayoutParams.WRAP_CONTENT)
//        // 点击popuwindow外让其消失
//        mPopupWindow.isOutsideTouchable = true
//        var call = mPopView.findViewById<Button>(R.id.btn_call_phone)
//        var copy = mPopView.findViewById<Button>(R.id.btn_copy_phone)
//        var cancel = mPopView.findViewById<Button>(R.id.btn_cancel)
//        call.setOnClickListener{
//            this.openCall(text)
//            if (mPopupWindow.isShowing) {
//                mPopupWindow.dismiss();
//            }
//        }
//        copy.setOnClickListener{
//            this.copyToShear(text)
//            if (mPopupWindow.isShowing) {
//                mPopupWindow.dismiss();
//            }
//        }
//        cancel.setOnClickListener{
//            if (mPopupWindow.isShowing) {
//                mPopupWindow.dismiss();
//            }
//        }
//        mPopupWindow.setOnDismissListener {
//            val params = this.getWindow().getAttributes()
//            params.alpha = 1f
//            this.window.attributes = params
//        }
//        if (mPopupWindow.isShowing) {
//            mPopupWindow.dismiss();
//        } else {
//            val params = this.getWindow().getAttributes()
//            params.alpha = 0.7f
//            this.window.attributes = params
//            // 设置PopupWindow 显示的形式 底部或者下拉等
//            // 在某个位置显示
//            mPopupWindow.showAtLocation(mPopView, Gravity.BOTTOM, 0, 0);
//            // 作为下拉视图显示
//            // mPopupWindow.showAsDropDown(mPopView, Gravity.CENTER, 200, 300);
//        }
//    }else{
//        signPermission(arrayOf(android.Manifest.permission.CALL_PHONE))
//    }
//
//}