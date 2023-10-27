package com.hq.generalsecurity.widget.img

import android.graphics.Bitmap
import android.view.View
import com.bumptech.glide.Glide
import com.hq.generalsecurity.BaseActivity
import com.hq.generalsecurity.databinding.LayerStandardImgHideBinding
import com.hq.generalsecurity.expand.*
import com.hq.generalsecurity.widget.form.Parent
import com.hq.tool.bitmap.base64ToBitmap
import com.hq.tool.bitmap.bitmapToBase64
import com.hq.tool.bitmap.saveBitmapFile
import com.hq.tool.system.openCameraAndGalleryWindowPro
import com.hq.tool.system.openCameraPro
import com.hq.tool.system.openGalleryPro
import com.hq.tool.toStringPro

class ImgHideSingle: Parent<ImgSet, LayerStandardImgHideBinding>() {

    var b:Bitmap?=null

    override fun getViewBindingCls(): Class<LayerStandardImgHideBinding> {
         return LayerStandardImgHideBinding::class.java
    }

    override fun init(data:MutableMap<String,Any>?) {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        viewBinding.showDown.setOnClickListener {
            viewBinding.imgContent.visibility=if(viewBinding.imgContent.visibility== View.GONE){View.VISIBLE}else{View.GONE}
        }

        if (data!=null&&line.serviceName.isNotEmpty()) {
            line.data = data[line.serviceName].toStringPro()
        }
        if (line.data!=""&&line.data.isNotEmpty()){
            when(line.format){
                ImgFormat.Base64-> {
                    b=line.data.base64ToBitmap()
                    viewBinding.imgContent.setImageBitmap(b)
                }
                ImgFormat.Stream-> Glide.with(viewBinding.imgContent).load(line.data)
            }
        }else{
            when(line.placeholder){
                CacheType.Local->{
                    viewBinding.imgContent.setImageResource(Expand.getFieldValue("mipmap",line.placeholderName,viewBinding.imgContent.context))
                }
                CacheType.Net->{
                    Glide.with(viewBinding.imgContent).load(line.placeholderName)
                }
            }
        }
        when(line.onClick){
            ClickAction.Camera-> viewBinding.imgContent.setOnClickListener {
                BaseActivity.active.openCameraPro {
                    b = it
                    viewBinding.imgContent.setImageBitmap(b)
                }
            }
            ClickAction.Gallery->  viewBinding.imgContent.setOnClickListener {  BaseActivity.active.openGalleryPro {
                b=it
                viewBinding.imgContent.setImageBitmap(b)
            }  }
            ClickAction.CameraGallery->  viewBinding.imgContent.setOnClickListener {
                BaseActivity.active.openCameraAndGalleryWindowPro( {
                b=it
                viewBinding.imgContent.setImageBitmap(b)
            })  }
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
            ImgFormat.Base64-> b?.bitmapToBase64()
            ImgFormat.Stream-> b?.saveBitmapFile(Expand.getImgPath()+"/"+"${System.currentTimeMillis()}.jpg")
        }
    }

    override fun getTargetView(): View {
        return viewBinding.imgContent
    }
}