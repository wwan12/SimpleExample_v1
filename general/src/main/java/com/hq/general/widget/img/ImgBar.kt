package com.hq.general.widget.img

import android.graphics.Bitmap
import android.view.View
import com.bumptech.glide.Glide
import com.hq.general.BaseActivity
import com.hq.general.databinding.LayerStandardImgBarBinding
import com.hq.general.expand.*
import com.hq.general.extraction.DataExtraction
import com.hq.general.model.*
import com.hq.general.widget.form.Parent
import com.hq.tool.bitmap.base64ToBitmap
import com.hq.tool.bitmap.bitmapToBase64
import com.hq.tool.bitmap.saveBitmapFile
import com.hq.tool.misc.Reflect
import com.hq.tool.system.openCameraAndGalleryWindowPro
import com.hq.tool.system.openCameraPro
import com.hq.tool.system.openGalleryPro
import com.hq.tool.toStringPro

class ImgBar: Parent<ImgSet, LayerStandardImgBarBinding>() {

    var b:Bitmap?=null

    override fun getViewBindingCls(): Class<LayerStandardImgBarBinding> {
         return LayerStandardImgBarBinding::class.java
    }

    override fun init() {
        if (line.serviceName.isNotEmpty()) {
            line.data = data.data.toStringPro()
        }
        if (line.data!=""&&line.data.isNotEmpty()){
            when(line.format){
                FileFormat.Base64-> {
                    b=line.data.base64ToBitmap()
                    viewBinding.imgContent.setImageBitmap(b)
                }
                FileFormat.Stream-> Glide.with(viewBinding.imgContent).load(line.data)
            }
        }else{
            when(line.placeholder){
                CacheType.Local->{
                    viewBinding.imgContent.setImageResource(Reflect.getFieldValue("mipmap",line.placeholderName,viewBinding.imgContent.context))
                }
                CacheType.Net->{
                    Glide.with(viewBinding.imgContent).load(line.placeholderName)
                }
            }
        }
        when(line.onClick) {
            ClickAction.Camera -> viewBinding.imgContent.setOnClickListener {
                com.hq.general.BaseActivity.active.openCameraPro {
                    if (it!=null){
                        b = it
                        viewBinding.imgContent.setImageBitmap(b)
                    }
                }
            }
            ClickAction.Gallery -> viewBinding.imgContent.setOnClickListener {
                com.hq.general.BaseActivity.active.openGalleryPro {
                    if (it!=null){
                        b = it
                        viewBinding.imgContent.setImageBitmap(b)
                    }
                }
            }
            ClickAction.CameraGallery -> viewBinding.imgContent.setOnClickListener {
                com.hq.general.BaseActivity.active.openCameraAndGalleryWindowPro({
                    if (it!=null){
                        b = it
                        viewBinding.imgContent.setImageBitmap(b)
                    }
                })
            }
            ClickAction.Go -> {

            }
        }

    }

    override fun check(): Boolean {
        if (line.must){
            return b!=null
        }
        return true
    }

    override fun data(): Any? {
        return when(line.format){
            FileFormat.Base64-> b?.bitmapToBase64()
            FileFormat.Stream-> b?.saveBitmapFile(Expand.getImgPath()+"/"+"${System.currentTimeMillis()}.jpg")
        }
    }

    override fun getTargetView(): View {
        return viewBinding.imgContent
    }
    override fun refresh() {

    }
}