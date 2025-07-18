package com.hq.tool.system

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.FileProvider
import com.hq.tool.R
import com.hq.tool.bitmap.drawable2Bitmap
import com.hq.tool.loge
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by lenovo on 2017/12/5.
 */
val CAMERA_REQUEST = 1000
val GALLERY_REQUEST = 2000
var cameraUri:Uri? = null


fun Activity.openCameraAndGalleryWindowPro(call:(Bitmap?)->Unit,delete: Boolean=false) {
    // 将布局文件转换成View对象，popupview 内容视图
    val mPopView= if (delete){
        this.layoutInflater.inflate(R.layout.camera_gallery_delete_window, null)
    }else{
        this.layoutInflater.inflate(R.layout.camera_gallery_window, null)
    }
  //  val mPopView = this.layoutInflater.inflate(R.layout.camera_gallery_window, null)
    // 将转换的View放置到 新建一个popuwindow对象中

    val mPopupWindow = PopupWindow(mPopView,
        this.windowManager.defaultDisplay.width-128,
        LinearLayout.LayoutParams.WRAP_CONTENT)
    // 点击popuwindow外让其消失
    mPopupWindow.setOutsideTouchable(true)
    var openCamera = mPopView.findViewById<Button>(R.id.btn_take_photo)
    var openGallery = mPopView.findViewById<Button>(R.id.btn_pick_photo)
    var deleteBitmap = mPopView.findViewById<Button>(R.id.btn_delete_photo)
    var cancel = mPopView.findViewById<Button>(R.id.btn_cancel)
    openCamera.setOnClickListener{
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        openCameraPro(call)
    }
    openGallery.setOnClickListener{
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        openGalleryPro(call)
    }
    cancel.setOnClickListener{
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
    deleteBitmap?.setOnClickListener{
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        call(null)
    }
    mPopupWindow.setOnDismissListener {
        val params = this.getWindow().getAttributes()
        params.alpha = 1f
        this.getWindow().setAttributes(params)
    }
    if (mPopupWindow.isShowing()) {
        mPopupWindow.dismiss();
    } else {
        val params = this.getWindow().getAttributes()
        params.alpha = 0.7f
        this.getWindow().setAttributes(params)
        // 设置PopupWindow 显示的形式 底部或者下拉等
        // 在某个位置显示
        mPopupWindow.showAtLocation(mPopView, Gravity.BOTTOM, 0, 0);
        // 作为下拉视图显示
        // mPopupWindow.showAsDropDown(mPopView, Gravity.CENTER, 200, 300);
    }
}
fun Activity.openGalleryPro(call:(Bitmap?)->Unit) {

    PictureSelector.create(this)
        .openGallery(SelectMimeType.ofImage())
        .setMinSelectNum(1)
        .setMaxSelectNum(1)
        .setImageEngine(GlideEngine.createGlideEngine())
        .forResult(object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: ArrayList<LocalMedia>) {
                val filePath= if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    result[0].availablePath;
                } else {
                    result[0].realPath;
                }
                var b=BitmapFactory.decodeFile(filePath)
              //  var b=BitmapFactory.decodeFile(result[0].path)
                if  (b==null){
                    val uri= Uri.parse(result[0].path)
                    //   call(BitmapFactory.decodeStream(FileInputStream(file) ))
                    var pfd: ParcelFileDescriptor?
                    if (uri != null) {
                        pfd = this@openGalleryPro.contentResolver.openFileDescriptor(uri, "r")
                        if (pfd != null) {
                            val bitmap = BitmapFactory.decodeFileDescriptor(pfd.fileDescriptor);
                            // show the bitmap, or do something else.
                            pfd.close()
                            call(bitmap)
                        }
                    }

                }else{
                    call(b)
                }
            }
            override fun onCancel() {
                call(null)
            }


        })
}


fun Activity.openGalleryPathPro(call:(String?)->Unit) {

    PictureSelector.create(this)
        .openGallery(SelectMimeType.ofImage())
        .setMinSelectNum(1)
        .setMaxSelectNum(1)
        .setImageEngine(GlideEngine.createGlideEngine())
        .forResult(object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: ArrayList<LocalMedia>) {
                call(result[0].path)
            }
            override fun onCancel() {
                call(null)
            }


        })
}
fun Activity.openCameraPathPro(call:(String?)->Unit): Unit {
    PictureSelector.create(this)
        .openCamera(SelectMimeType.ofImage())
        .forResult(object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: ArrayList<LocalMedia>) {
                call(result[0].path)
            }
            override fun onCancel() {
                call(null)
            }
        })
}


fun Activity.openCameraPro(call:(Bitmap?)->Unit): Unit {

    PictureSelector.create(this)
        .openCamera(SelectMimeType.ofImage())
        .forResult(object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: ArrayList<LocalMedia>) {
                //  if(result[0]!=null&&result[0].path!=null)
                val filePath =
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        result[0].availablePath
                    } else {
                        result[0].realPath
                    }
                val b = BitmapFactory.decodeFile(filePath)
                if  (b==null){
                    val uri= Uri.parse(result[0].path)
                    //   call(BitmapFactory.decodeStream(FileInputStream(file) ))
                    val pfd:ParcelFileDescriptor?
                    if (uri != null) {
                        pfd = this@openCameraPro.contentResolver.openFileDescriptor(uri, "r")
                        if (pfd != null) {
                            val bitmap = BitmapFactory.decodeFileDescriptor(pfd.fileDescriptor);
                            // show the bitmap, or do something else.
                            pfd.close()
                            call(bitmap)
                            contentResolver.delete(
                                uri,
                                null,
                                null
                            )
                        }
                    }
                }else{
                    call(b)
                    File(filePath).delete()
                }
                result[0].path.loge()
            }
            override fun onCancel() {
                call(null)
            }
        })
}


fun Activity.openCameraAndGalleryWindow() {
    // 将布局文件转换成View对象，popupview 内容视图
    val mPopView = this.layoutInflater.inflate(R.layout.camera_gallery_window, null)
    // 将转换的View放置到 新建一个popuwindow对象中

    val mPopupWindow = PopupWindow(mPopView,
            this.windowManager.defaultDisplay.width-128,
            LinearLayout.LayoutParams.WRAP_CONTENT)
    // 点击popuwindow外让其消失
    mPopupWindow.setOutsideTouchable(true)
    var openCamera = mPopView.findViewById<Button>(R.id.btn_take_photo)
    var openGallery = mPopView.findViewById<Button>(R.id.btn_pick_photo)
    var cancel = mPopView.findViewById<Button>(R.id.btn_cancel)
    openCamera.setOnClickListener{
        cameraUri = this.openCamera()
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
    openGallery.setOnClickListener{
        this.openGallery()
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
    cancel.setOnClickListener{
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
    mPopupWindow.setOnDismissListener {
        val params = this.getWindow().getAttributes()
        params.alpha = 1f
        this.getWindow().setAttributes(params)
    }
    if (mPopupWindow.isShowing()) {
        mPopupWindow.dismiss();
    } else {
        val params = this.getWindow().getAttributes()
        params.alpha = 0.7f
        this.getWindow().setAttributes(params)
        // 设置PopupWindow 显示的形式 底部或者下拉等
        // 在某个位置显示
        mPopupWindow.showAtLocation(mPopView, Gravity.BOTTOM, 0, 0);
        // 作为下拉视图显示
        // mPopupWindow.showAsDropDown(mPopView, Gravity.CENTER, 200, 300);
    }
}

fun Activity.openGallery() {
    if (!checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)){
        signPermission(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
        return
    }
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    this.startActivityForResult(intent, GALLERY_REQUEST)
}

fun Activity.openCamera() :Uri?{
    if (!checkPermission(Manifest.permission.CAMERA)){
        signPermission(Manifest.permission.CAMERA)
        return null
    }
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH) + 1
    val day = c.get(Calendar.DAY_OF_MONTH)
    val imgName = year.toString() + "_" + month + "_" + day + "(" + System.currentTimeMillis() + ").jpg"
    var f = File(this.cacheDir.path, imgName)
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M){
        val absolutePath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "zhly"
        val f1 = File(absolutePath)
        f1.mkdirs()
        f = File(absolutePath , imgName)
        f.createNewFile()
    }

    val contentUri: Uri
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N||Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
        contentUri = FileProvider.getUriForFile(this, this.applicationInfo.packageName+".fileProvider", f)
        //            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    } else {
        contentUri = Uri.fromFile(f)
    }
    if ( getDeviceBrand().toUpperCase().contains("VIVO")|| getDeviceBrand().toUpperCase().contains("OPPO")){
        intent.putExtra(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA, contentUri)
    }else{
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
    }
    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
    intent.putExtra("return-data", false)
    this.startActivityForResult(intent, CAMERA_REQUEST)
    cameraUri =contentUri
    return  contentUri
}

fun getDeviceBrand(): String {
    return android.os.Build.BRAND
}
//读取相册获取的图片uri
fun Uri.handleImageOnKitKat(activity: Activity): Bitmap? {
    var imagePath: String? = null
//        val uri = Uri.parse(uriString)
    if (DocumentsContract.isDocumentUri(activity, this)) {
        val docId = DocumentsContract.getDocumentId(this)
        if ("com.android.providers.media.documents" == this.authority) {
            val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val selection = MediaStore.Images.Media._ID + "=" + id
            imagePath = getImagePath(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
        } else if ("com.android.providers.downloads.documents" == this.authority) {
            val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(docId)!!)
            imagePath = getImagePath(activity, contentUri, null)
        }
    } else if ("content".equals(this.scheme, ignoreCase = true)) {
        imagePath = getImagePath(activity, this, null)
    }
    return BitmapFactory.decodeFile(imagePath)
}

private fun getImagePath(activity: Activity, uri: Uri, selection: String?): String? {
    var path: String? = null
    val cursor = activity.contentResolver.query(uri, null, selection, null, null)
    if (cursor != null) {
        if (cursor.moveToFirst()) {
            val num = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            path = cursor.getString(num)
        }
        cursor.close()
    }
    return path
}

/**
 * 获取相机照出来的图片
 */
fun Uri.getCameraImg(activity: Activity): Bitmap? {
    var stream: InputStream? = null
    val sCompatUseCorrectStreamDensity = activity.applicationInfo.targetSdkVersion > Build.VERSION_CODES.M

    try {
        stream = activity.contentResolver.openInputStream(this)
        val dar = Drawable.createFromResourceStream(if (sCompatUseCorrectStreamDensity)
            activity.resources else null, null, stream, null)
        return dar.drawable2Bitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        return BitmapFactory.decodeStream(stream)

    } finally {
        stream?.close()
    }
    return null
}

fun Intent.getCameraUri(): Uri? {
    return cameraUri
}

fun Uri.toFile(context: Context): File? {
    var path: String? = null
    if (DocumentsContract.isDocumentUri(context, this)) {
        val docId = DocumentsContract.getDocumentId(this)
        if ("com.android.providers.media.documents" == this.authority) {
            val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val selection = MediaStore.Images.Media._ID + "=" + id
            path = getImagePath(context as Activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
        } else if ("com.android.providers.downloads.documents" == this.authority) {
            val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(docId)!!)
            path = getImagePath(context as Activity, contentUri, null)
        }
    } else if ("content".equals(this.scheme, ignoreCase = true)) {
        path = getImagePath(context as Activity, this, null)
    }
    return File(path)
}