package com.aisino.tool.system

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
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.FileProvider
import com.aisino.tool.R
import com.aisino.tool.bitmap.drawable2Bitmap
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.InputStream
import java.util.*



/**
 * Created by lenovo on 2017/12/5.
 */


//var uri: Uri?=null
////        var bitmap: Bitmap?=null
//if (requestCode == GALLERY_REQUEST &&resultCode== RESULT_OK) {
//    uri=data?.data
////            bitmap=uri?.handleImageOnKitKat(this)
//
//}
//if (requestCode == CAMERA_REQUEST &&resultCode== RESULT_OK) {
//    uri= cameraUri
////            bitmap=uri?.getCameraImg(this)
//}
//if (uri!=null){
//    val f = File(this.filesDir.path, "crop.jpg")
//    UCrop.of(uri, Uri.fromFile(f))
//            .start(this)
//}
//if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
//    val resultUri = data?.let { UCrop.getOutput(it) }
//    seal_photo.setImageURI(resultUri)
//    sealBitmap= seal_photo.drawable.toBitmap().setBitmapSize(512,512)
//} else if (resultCode == UCrop.RESULT_ERROR) {
//    val cropError = data?.let { UCrop.getError(it) }
//}

val CAMERA_REQUEST = 1000
val GALLERY_REQUEST = 2000
val CORP_REQUEST=3000
var cameraUri:Uri? = null
var appId=""

fun Activity.openCameraAndGalleryWindow() {
    // 将布局文件转换成View对象，popupview 内容视图
    val mPopView = this.layoutInflater.inflate(R.layout.camera_gallery_window, null)
    // 将转换的View放置到 新建一个popuwindow对象中

    val mPopupWindow = PopupWindow(mPopView,
            this.windowManager.defaultDisplay.width-128,
            LinearLayout.LayoutParams.WRAP_CONTENT)
    // 点击popuwindow外让其消失
    mPopupWindow.isOutsideTouchable = true
    var openCamera = mPopView.findViewById<Button>(R.id.btn_take_photo)
    var openGallery = mPopView.findViewById<Button>(R.id.btn_pick_photo)
    var cancel = mPopView.findViewById<Button>(R.id.btn_cancel)
    openCamera.setOnClickListener{
        cameraUri= this.openCamera()
        if (mPopupWindow.isShowing) {
            mPopupWindow.dismiss();
        }
    }
    openGallery.setOnClickListener{
        this.openGallery()
        if (mPopupWindow.isShowing) {
            mPopupWindow.dismiss();
        }
    }
    cancel.setOnClickListener{
        if (mPopupWindow.isShowing) {
            mPopupWindow.dismiss();
        }
    }
    mPopupWindow.setOnDismissListener {
        val params = this.getWindow().getAttributes()
        params.alpha = 1f
        this.window.attributes = params
    }
    if (mPopupWindow.isShowing) {
        mPopupWindow.dismiss();
    } else {
        val params = this.getWindow().getAttributes()
        params.alpha = 0.7f
        this.window.attributes = params
        // 设置PopupWindow 显示的形式 底部或者下拉等
        // 在某个位置显示
        mPopupWindow.showAtLocation(mPopView, Gravity.BOTTOM, 0, 0);
        // 作为下拉视图显示
        // mPopupWindow.showAsDropDown(mPopView, Gravity.CENTER, 200, 300);
    }
}

fun Activity.openGallery() {
    this.signPermission(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        this.startActivityForResult(intent, GALLERY_REQUEST)
    }
}

fun Activity.openCamera() :Uri?{
    var contentUri: Uri? = null
    this.signPermission(arrayOf(Manifest.permission.CAMERA)) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)
        val imgName = year.toString() + "_" + month + "_" + day + "(" + System.currentTimeMillis() + ").jpg"
        val f = File(this.filesDir.path, imgName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N||Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (appId.equals("")){
                appId=this.getAppId()
            }
            contentUri = FileProvider.getUriForFile(this, "${appId}.fileProvider", f)
        } else {
            contentUri = Uri.fromFile(f)
        }
        if ( getDeviceBrand().toUpperCase().contains("VIVO")||getDeviceBrand().toUpperCase().contains("OPPO")){
            intent.putExtra(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA, contentUri)
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
        intent.putExtra("return-data", false)
        this.startActivityForResult(intent, CAMERA_REQUEST)
    }

    return  contentUri
}

/**
 * 裁剪图片
 */
@Deprecated("xiaomi，三星手机不兼容")
fun Activity.cropPicture(cropFile: File):Uri {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH) + 1
    val day = c.get(Calendar.DAY_OF_MONTH)
    val imgName = "crop_"+ "(" + System.currentTimeMillis() + ").jpg"
    val f = File(this.filesDir.path, imgName)
    val contentUri = Uri.fromFile(f)
    val cropIntent = Intent("com.android.camera.action.CROP")
    val pictureUri= if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        Uri.fromFile(cropFile)
    } else {
        //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
        if (appId.equals("")){
            appId=this.getAppId()
        }
        FileProvider.getUriForFile(this, "${appId}.fileProvider", cropFile)
    }
    cropIntent.setDataAndType(pictureUri, "image/*") //7.0以上 输入的uri需要是provider提供的
    // 开启裁剪：打开的Intent所显示的View可裁剪
    cropIntent.putExtra("crop", true)
    // 裁剪宽高比
//    cropIntent.putExtra("aspectX", 1f)
//    cropIntent.putExtra("aspectY", 1f)
    // 裁剪输出大小
    cropIntent.putExtra("outputX", 240)
    cropIntent.putExtra("outputY", 240)
    cropIntent.putExtra("scale", true)
    /**
     * return-data
     * 这个属性决定onActivityResult 中接收到的是什么数据类型，
     * true data将会返回一个bitmap
     * false，则会将图片保存到本地并将我们指定的对应的uri。
     */
    cropIntent.putExtra("return-data", true)
    // 当 return-data 为 false 的时候需要设置输出的uri地址
//        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri) //输出的uri为普通的uri，通过provider提供的uri会出现无法保存的错误
    // 图片输出格式
    cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
    cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //不加会出现无法加载此图片的错误
    cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) // 这两句是在7.0以上版本当targeVersion大于23时需要
    startActivityForResult(cropIntent, CORP_REQUEST)
    return contentUri
}

/**
 * 裁剪图片 使用uCrop
 */
fun Activity.cropPicture(crop: Uri):Unit {
    val f = File(this.filesDir.path, "crop.jpg")
   val outUri= Uri.fromFile(f)
    UCrop.of(crop, outUri).start(this)
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
        val dar = Drawable.createFromResourceStream(if (sCompatUseCorrectStreamDensity) activity.resources else null, null, stream, null)
        return dar.drawable2Bitmap()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (stream != null) {
            stream.close()
        }
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