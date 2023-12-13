package com.hq.general.widget.img

import android.graphics.Bitmap
import android.view.View
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hq.general.databinding.ItemImgListBinding
import com.hq.general.databinding.LayerStandardImgListBinding
import com.hq.general.expand.*
import com.hq.general.extraction.DataExtraction
import com.hq.general.model.*
import com.hq.general.widget.form.Parent
import com.hq.tool.bitmap.base64ToBitmap
import com.hq.tool.bitmap.bitmapToBase64
import com.hq.tool.bitmap.downloadBitmap
import com.hq.tool.bitmap.saveBitmapFile
import com.hq.tool.misc.Reflect
import com.hq.tool.system.openCameraAndGalleryWindowPro
import com.hq.tool.system.openCameraPro
import com.hq.tool.system.openGalleryPro
import com.hq.tool.toStringPro
import com.hq.tool.widget.adapter.SimpleBindAdapter
import java.lang.reflect.Type


class ImgHideList: Parent<ImgSet, LayerStandardImgListBinding>() {

    var bs:ArrayList<Bitmap?> = arrayListOf()

    //当前照片数
    var number=0

    override fun getViewBindingCls(): Class<LayerStandardImgListBinding> {
         return LayerStandardImgListBinding::class.java
    }

    override fun init() {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        viewBinding.imgList.visibility=View.GONE
        viewBinding.showDown.setOnClickListener {
            viewBinding.imgList.visibility=if(viewBinding.imgList.visibility== View.GONE){View.VISIBLE}else{View.GONE}
        }
        if (line.serviceName.isNotEmpty()) {
            line.data = data.data.toStringPro()
        }
        if (line.data!=""&&line.data.isNotEmpty()){
            val type: Type = object : TypeToken<ArrayList<String>>() {}.type
            val ls= Gson().fromJson<ArrayList<String>>(line.data,type)
            when(line.format){
                FileFormat.Base64-> {
                    for (l in ls){
                        bs.add(l.base64ToBitmap())
                    }
                    refresh()
                }
                FileFormat.Stream-> {
                    for (l in ls){
                        bs.add(null)
                        l.downloadBitmap{
                            bs[ls.indexOf(l)]=it
                            com.hq.general.BaseActivity.active.runOnUiThread {
                                refresh()
                            }
                        }
                    }
                }
            }
        }

        viewBinding.imgList.adapter=SimpleBindAdapter(com.hq.general.BaseActivity.active,ItemImgListBinding::class.java, bs)
        {b,imgView->
            if (b!=null){
                imgView.imgContent.setImageBitmap(b)
            }else{
                when(line.placeholder){
                    CacheType.Local->{
                        imgView.imgContent.setImageResource(Reflect.getFieldValue("mipmap",line.placeholderName,
                            com.hq.general.BaseActivity.active))
                    }
                    CacheType.Net->{
                        Glide.with(imgView.imgContent).load(line.placeholderName)
                    }
                }
            }
        }
        when(line.onClick){
            ClickAction.Camera-> viewBinding.imgList.setOnItemClickListener { parent, view, position, id ->
                com.hq.general.BaseActivity.active.openCameraPro {
                    if (it!=null){
                        bs[position] = it
                        refresh()
                    }
                }
            }
            ClickAction.Gallery->  viewBinding.imgList.setOnItemClickListener { parent, view, position, id ->
                com.hq.general.BaseActivity.active.openGalleryPro {
                    if (it!=null){
                        bs[position] = it
                        refresh()
                    }

                }
            }
            ClickAction.CameraGallery-> viewBinding.imgList.setOnItemClickListener { parent, view, position, id ->
                com.hq.general.BaseActivity.active.openCameraAndGalleryWindowPro({
                    if (it!=null){
                        bs[position] = it
                        refresh()
                    }
                })
            }
        }

    }

    override fun check(): Boolean {
        if (line.must){

            return number>=line.list.minCount
        }
        return true
    }

    override fun data(): Any? {
        val list= arrayListOf<String>()
        for (b in bs){
            if (b!=null){
                when(line.format){
                    FileFormat.Base64-> list.add(b.bitmapToBase64())
                    FileFormat.Stream-> {
                       val file= b.saveBitmapFile(Expand.getImgPath()+"/"+"${System.currentTimeMillis()}.jpg")
                        if (file!=null){
                            list.add(file.absolutePath)
                        }

                    }
                }
            }
        }
        return list
    }

    override fun getTargetView(): View {
        return viewBinding.imgList
    }

    override fun refresh(): Unit {
        number=0
        for (b in bs){
            if (b!=null){
                number++
            }
        }
        when(line.list.model){
            ListModel.Fixed-> {
                if (line.list.maxCount < bs.size) {
                    for (i in 0 until line.list.maxCount-bs.size){
                        bs.add(null)
                    }
                }
            }
            ListModel.Grow-> {
                if (line.list.maxCount < bs.size) {
                    if (bs[bs.size-1]!=null){
                        bs.add(null)
                    }
                }
            }
            ListModel.Infinite->{
                if (bs[bs.size-1]!=null){
                    bs.add(null)
                }
            }
        }
        (viewBinding.imgList.adapter as BaseAdapter).notifyDataSetChanged()
    }

}