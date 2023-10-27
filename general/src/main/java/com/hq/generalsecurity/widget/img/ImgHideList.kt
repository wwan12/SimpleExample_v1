package com.hq.generalsecurity.widget.img

import android.graphics.Bitmap
import android.view.View
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hq.generalsecurity.BaseActivity
import com.hq.generalsecurity.databinding.ItemImgListBinding
import com.hq.generalsecurity.databinding.LayerStandardImgListBinding
import com.hq.generalsecurity.expand.*
import com.hq.generalsecurity.widget.form.Parent
import com.hq.tool.bitmap.base64ToBitmap
import com.hq.tool.bitmap.bitmapToBase64
import com.hq.tool.bitmap.downloadBitmap
import com.hq.tool.bitmap.saveBitmapFile
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

    override fun init(data:MutableMap<String,Any>?) {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        viewBinding.imgList.visibility=View.GONE
        viewBinding.showDown.setOnClickListener {
            viewBinding.imgList.visibility=if(viewBinding.imgList.visibility== View.GONE){View.VISIBLE}else{View.GONE}
        }
        if (data!=null&&line.serviceName.isNotEmpty()) {
            line.data = data[line.serviceName].toStringPro()
        }
        if (line.data!=""&&line.data.isNotEmpty()){
            val type: Type = object : TypeToken<ArrayList<String>>() {}.type
            val ls= Gson().fromJson<ArrayList<String>>(line.data,type)
            when(line.format){
                ImgFormat.Base64-> {
                    for (l in ls){
                        bs.add(l.base64ToBitmap())
                    }
                    refresh()
                }
                ImgFormat.Stream-> {
                    for (l in ls){
                        bs.add(null)
                        l.downloadBitmap{
                            bs[ls.indexOf(l)]=it
                            BaseActivity.active.runOnUiThread {
                                refresh()
                            }
                        }
                    }
                }
            }
        }

        viewBinding.imgList.adapter=SimpleBindAdapter(BaseActivity.active,ItemImgListBinding::class.java, bs)
        {b,imgView->
            if (b!=null){
                imgView.imgContent.setImageBitmap(b)
            }else{
                when(line.placeholder){
                    CacheType.Local->{
                        imgView.imgContent.setImageResource(Expand.getFieldValue("mipmap",line.placeholderName,BaseActivity.active))
                    }
                    CacheType.Net->{
                        Glide.with(imgView.imgContent).load(line.placeholderName)
                    }
                }
            }
        }
        when(line.onClick){
            ClickAction.Camera-> viewBinding.imgList.setOnItemClickListener { parent, view, position, id ->
                BaseActivity.active.openCameraPro {
                    bs[position] = it
                    refresh()
                }
            }
            ClickAction.Gallery->  viewBinding.imgList.setOnItemClickListener { parent, view, position, id ->
                BaseActivity.active.openGalleryPro {
                    bs[position] = it
                    refresh()
                }
            }
            ClickAction.CameraGallery-> viewBinding.imgList.setOnItemClickListener { parent, view, position, id ->
                BaseActivity.active.openCameraAndGalleryWindowPro({
                    bs[position] = it
                    refresh()
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
                    ImgFormat.Base64-> list.add(b.bitmapToBase64())
                    ImgFormat.Stream-> {
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

    fun refresh(): Unit {
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