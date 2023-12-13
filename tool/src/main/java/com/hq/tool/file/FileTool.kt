package com.hq.tool.file

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.Gravity
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.FileProvider
import com.hq.tool.R
import com.hq.tool.animation.LoadingDialog
import com.hq.tool.databinding.FileSelectWindowBinding
import com.hq.tool.http.Http
import com.hq.tool.model.filepicker.ExplorerConfig
import com.hq.tool.model.filepicker.FilePicker
import com.hq.tool.model.filepicker.annotation.ExplorerMode
import com.hq.tool.system.*
import com.hq.tool.toast
import okhttp3.Headers
import java.io.File

object FileTool {

    fun openFilePicker(activity: Activity,supports:ArrayList<String>,call:(File?)->Unit): Unit {
        if (!activity.checkPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            activity.signPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            return
        }

        val config = ExplorerConfig(activity)
        config.rootDir = Environment.getExternalStorageDirectory()
        config.isLoadAsync = false
        config.explorerMode = ExplorerMode.FILE
        config.isShowHomeDir = true
        config.isShowUpDir = true
        config.isShowHideDir = true
        val extensions = arrayOfNulls<String>(supports.size)
        config.allowExtensions = supports.toArray(extensions)
        config.setOnFileClickedListener(call)
        val filePicker = FilePicker(activity)
        filePicker.setExplorerConfig(config)
        filePicker.show()
    }

    fun openLocalFileWindow(activity: Activity, open:()->Unit,select:()->Unit) {
        // 将布局文件转换成View对象，popupview 内容视图
        val viewBinding=FileSelectWindowBinding.inflate(activity.layoutInflater,null,false)
        val mPopView= viewBinding.root
        // 将转换的View放置到 新建一个popuwindow对象中
        val mPopupWindow = PopupWindow(mPopView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        // 点击popuwindow外让其消失
        mPopupWindow.isOutsideTouchable = true
        viewBinding.btnDownloadOpen.setOnClickListener{
            if (mPopupWindow.isShowing) {
                mPopupWindow.dismiss()
            }
            open()

        }
        viewBinding.btnReSelect.setOnClickListener{
            if (mPopupWindow.isShowing) {
                mPopupWindow.dismiss();
            }
            select()
        }
        viewBinding.btnCancel.setOnClickListener{
            if (mPopupWindow.isShowing) {
                mPopupWindow.dismiss();
            }
        }

        mPopupWindow.setOnDismissListener {
            val params = activity.getWindow().getAttributes()
            params.alpha = 1f
            activity.getWindow().setAttributes(params)
        }
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            val params = activity.window.getAttributes()
            params.alpha = 0.7f
            activity.window.setAttributes(params)
            // 设置PopupWindow 显示的形式 底部或者下拉等
            // 在某个位置显示
            mPopupWindow.showAtLocation(mPopView, Gravity.BOTTOM, 0, 0);
        }
    }

    fun downloadFile(url:String,filePath:String,fileName:String,headers: Headers?,load: LoadingDialog,call: (File?) -> Unit): Unit {
        Http.download{
            this.url=url
            _headers= headers
            downloadPath= "${filePath}/${fileName}"
            success {
                call(File(downloadPath))
                load.dismiss()
            }
            fail {
                call(null)
                load.dismiss()
            }
        }
    }

    fun openFile(activity: Activity, file :File): Unit {
        if (file.exists()) {
            val openintent = Intent(Intent.ACTION_VIEW)
            openintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            openintent.addCategory(Intent.CATEGORY_DEFAULT)
            //val type = getMIMEType(file)
            val ext = MimeTypeMap.getFileExtensionFromUrl(file.path)
            val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)

            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
                openintent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                val uri= FileProvider.getUriForFile(activity.application, "${activity.packageName}.fileProvider",file)
//                    intent.data=uri
                openintent.setDataAndType(uri,type)
            }else{
                // 设置intent的data和Type属性。
                openintent.setDataAndType( /* uri */Uri.fromFile(file), type)
            }
            // 根据文件类型调用相应的软件
            if (activity.packageManager.resolveActivity(openintent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                activity.startActivity(openintent)
                activity.finish()
            }else{
                "未安装支持打开本文档格式的应用".toast(activity)
            }
        }else{
            "文件不存在".toast(activity)
        }

    }
}