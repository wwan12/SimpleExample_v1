package com.hq.general.widget.file

import android.Manifest
import android.app.Activity
import android.os.Environment
import android.util.Base64
import android.view.View
import com.hq.general.R
import com.hq.general.databinding.LayerStandardFileBinding
import com.hq.general.model.ClickAction
import com.hq.general.model.FileFormat
import com.hq.general.model.FileSet
import com.hq.general.widget.form.Parent
import com.hq.tool.http.Http
import com.hq.tool.loge
import com.hq.tool.model.filepicker.FilePicker
import com.hq.tool.toStringPro
import com.hq.tool.model.filepicker.annotation.ExplorerMode

import com.hq.tool.model.filepicker.ExplorerConfig
import com.hq.tool.system.checkPermission
import com.hq.tool.system.signPermission
import java.io.*
import java.lang.Exception


class FileDialog : Parent<FileSet, LayerStandardFileBinding>() {

    var file: File?=null

    var isDownload=false

    override fun getViewBindingCls(): Class<LayerStandardFileBinding> {
        return LayerStandardFileBinding::class.java
    }

    override fun init() {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        if (line.serviceName.isNotEmpty()) {
            when(data.data){
                is File->{
                    file=data.data as File
                    viewBinding.fileName.text=file?.name
                    viewBinding.fileContent.setImageResource(R.mipmap.file_edit)
                }
                is String->{
                    line.data = data.data.toStringPro()
                    if (line.data.contains("http://")||line.data.contains("https://")){
                        val i= line.data.lastIndexOf("/")
                        viewBinding.fileName.text=line.data.substring(i,line.data.length)
                    }else{

                    }
                    if (line.data.isNotEmpty()){
                        viewBinding.fileContent.setImageResource(R.mipmap.file_edit)
                    }
                }
                else->{

                }
            }
        }
        viewBinding.fileContent.setOnClickListener {
            when (line.onClick) {
                ClickAction.File -> {
                    if (!(viewBinding.fileContent.context as Activity).checkPermission(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        (viewBinding.fileContent.context as Activity).signPermission(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        return@setOnClickListener
                    }

                    val config = ExplorerConfig(viewBinding.root.context)
                    config.rootDir = Environment.getExternalStorageDirectory()
                    config.isLoadAsync = false
                    config.explorerMode = ExplorerMode.FILE
                    config.isShowHomeDir = true
                    config.isShowUpDir = true
                    config.isShowHideDir = true
                    val extensions = arrayOfNulls<String>(line.supports.size)
                    config.allowExtensions = line.supports.toArray(extensions)
                    config.setOnFilePickedListener {
                        viewBinding.fileContent.setImageResource(R.mipmap.file_edit)
                        viewBinding.fileName.text = it.name
                        file = it
                        line.data=""
                    }
                    val filePicker = FilePicker(viewBinding.root.context as Activity)
                    filePicker.setExplorerConfig(config)
                    filePicker.show()

                }
                ClickAction.Go -> {
                    if (line.data.contains("http://") || line.data.contains("https://")) {
                        Http.download {
                            url = line.data
                        }
                    }
                }

            }
            onClick?.invoke(this)
        }
    }

    override fun data(): Any? {
        if (line.data.isNotEmpty()){
            return line.data
        }
        if (file==null||file?.exists()==false){
            return null
        }
        val map= mutableMapOf<String,Any>()
         when(line.format){
            FileFormat.Base64-> {
               val fis=  FileInputStream(file)
                val bos = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var len=0
                while (fis.read(buffer).also { len = it } != -1) {
                    bos.write(buffer, 0, len)
                }
                val bytearray: ByteArray = bos.toByteArray()
                val fileBase64= Base64.encodeToString(bytearray, Base64.NO_WRAP)
                fileBase64.loge("fileBase64")
                bos.close()
                val sns= line.serviceName.split(",")
                map[sns[0]]= fileBase64
                map[sns[1]]= file!!.extension
            }
            FileFormat.Stream-> {
                map[line.serviceName]= file!!
            }
        }

        return map
    }
    fun base64ToFile(base64: String, fileName: String, savePath: String) {
        //前台在用Ajax传base64值的时候会把base64中的+换成空格，所以需要替换回来。
        //有的后台传的数据还有image:content..., 转换的时候都需要替换掉,转换之前尽量把base64字符串检查一遍
        var base64 = base64
        base64 = base64.replace(" ".toRegex(), "+")
        val file = File("$savePath/$fileName")

        val bytes: ByteArray = Base64.decode(base64, Base64.NO_WRAP) //base64编码内容转换为字节数组
        val bis = ByteArrayInputStream(bytes)
        val fos = FileOutputStream(file)
        val bos = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int = bis.read(buffer)
        while (length != -1) {
            bos.write(buffer, 0, length)
            length = bis.read(buffer)
            fos.write(buffer)
        }
        bos.flush()

        fos.close()
        bis.close()
        bos.close()
    }
    
    
    override fun check(): Boolean {
        return if (line.must){
            file!=null||line.data.isNotEmpty()
        }else{
            true
        }

    }

    override fun getTargetView(): View {
        return viewBinding.fileContent
    }

    override fun refresh() {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        if (line.serviceName.isNotEmpty()) {
            when(data.data){
                is File->{
                    file=data.data as File
                    viewBinding.fileName.text=file?.name
                    viewBinding.fileContent.setImageResource(R.mipmap.file_edit)
                    line.data=""
                }
                is String->{
                    line.data = data.data.toStringPro()
                    if (line.data.contains("http://")||line.data.contains("https://")){
                        val i= line.data.lastIndexOf("/")
                        viewBinding.fileName.text=line.data.substring(i,line.data.length)
                    }else{

                    }
                    if (line.data.isNotEmpty()){
                        viewBinding.fileContent.setImageResource(R.mipmap.file_edit)
                    }

                }
            }
        }
    }

}