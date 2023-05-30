package com.hq.generalsecurity.formwidget

import android.graphics.Bitmap
import android.view.View
import com.bumptech.glide.Glide
import com.hq.generalsecurity.databinding.LayerStandardImgBinding
import com.hq.generalsecurity.expand.Expand
import com.hq.generalsecurity.expand.ImgFormat
import com.hq.generalsecurity.expand.ImgOption
import com.hq.generalsecurity.expand.ImgSet
import com.hq.tool.bitmap.base64ToBitmap
import com.hq.tool.bitmap.bitmapToBase64
import com.hq.tool.bitmap.saveBitmapFile
import com.hq.tool.system.openCameraAndGalleryWindowPro
import com.hq.tool.system.openCameraPro
import com.hq.tool.system.openGalleryPro

class Img:Parent<ImgSet, LayerStandardImgBinding>() {

    var b:Bitmap?=null

    override fun getViewBindingCls(): Class<LayerStandardImgBinding> {
         return LayerStandardImgBinding::class.java
    }

    override fun init() {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        viewBinding.showDown.setOnClickListener {
            viewBinding.imgContent.visibility=if(viewBinding.imgContent.visibility== View.GONE){View.VISIBLE}else{View.GONE}
        }
        if (line.data.isNotEmpty()){
            when(line.format){
                ImgFormat.Base64-> {
                    b=line.data.base64ToBitmap()
                    viewBinding.imgContent.setImageBitmap(b)
                }
                ImgFormat.Stream-> Glide.with(viewBinding.imgContent).load(line.data)
            }
        }
        when(line.option){
            ImgOption.Camera-> viewBinding.imgContent.setOnClickListener { Expand.getMain().openCameraPro {
                b=it
                viewBinding.imgContent.setImageBitmap(b)
            } }
            ImgOption.Gallery->  viewBinding.imgContent.setOnClickListener {  Expand.getMain().openGalleryPro {
                b=it
                viewBinding.imgContent.setImageBitmap(b)
            }  }
            ImgOption.Total->  viewBinding.imgContent.setOnClickListener {  Expand.getMain().openCameraAndGalleryWindowPro( {
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
}