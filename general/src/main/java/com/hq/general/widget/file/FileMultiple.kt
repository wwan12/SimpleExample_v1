package com.hq.general.widget.file

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Base64
import android.view.View
import android.widget.BaseAdapter
import com.hq.general.R
import com.hq.general.databinding.ItemFileBinding
import com.hq.general.databinding.LayerStandardFileBinding
import com.hq.general.databinding.LayerStandardFileMultipleBinding
import com.hq.general.model.ClickAction
import com.hq.general.model.FileFormat
import com.hq.general.model.FileSet
import com.hq.general.widget.form.Parent
import com.hq.tool.file.FileTool
import com.hq.tool.http.Http
import com.hq.tool.loge
import com.hq.tool.misc.Reflect
import com.hq.tool.model.filepicker.FilePicker
import com.hq.tool.toStringPro
import com.hq.tool.model.filepicker.annotation.ExplorerMode

import com.hq.tool.model.filepicker.ExplorerConfig
import com.hq.tool.system.checkPermission
import com.hq.tool.system.signPermission
import com.hq.tool.toast
import com.hq.tool.widget.adapter.SimpleBindAdapter
import java.io.*
import java.lang.Exception


class FileMultiple : Parent<FileSet, LayerStandardFileMultipleBinding>() {

    val files: ArrayList<File> = arrayListOf()


    override fun getViewBindingCls(): Class<LayerStandardFileMultipleBinding> {
        return LayerStandardFileMultipleBinding::class.java
    }

    override fun init() {
        viewBinding.textTitle.text=  line.title
        viewBinding.addFile.text="新增${line.title}"
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        if (line.serviceName.isNotEmpty()) {

        }
        viewBinding.fileList.adapter=SimpleBindAdapter(viewBinding.root.context as Activity,ItemFileBinding::class.java, files)
        { file, itemFileBinding ->
            if (line.defIcon!=null){
                itemFileBinding.fileIcon.setImageResource(Reflect.getFieldValue("mipmap",line.defIcon!!,viewBinding.root.context))
            }
            itemFileBinding.fileName.text=file.name
            itemFileBinding.fileSize.text=FileTool.formatKMGByBytes(file.length())
            itemFileBinding.fileDelete.setOnClickListener {
                files.remove(file)
                (viewBinding.fileList.adapter as BaseAdapter).notifyDataSetChanged()
            }
        }
        viewBinding.fileList.setOnItemClickListener { parent, view, position, id ->
            onClick?.invoke(this)
            if (line.openBySys){
                FileTool.openLocalFileWindow(viewBinding.root.context as Activity,{
                    FileTool.openFile(viewBinding.root.context as Activity,files[position])
                })
            }
        }

        viewBinding.addFile.setOnClickListener {
            when (line.onClick) {
                ClickAction.File -> {
                    if (line.max<= files.size){
                        "已添加最大${line.title}数量".toast(viewBinding.root.context)
                        return@setOnClickListener
                    }
                    if (Build.VERSION.SDK_INT >= 31) {
                        if (!Environment.isExternalStorageManager()) {
                            val intent =
                                Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                            (viewBinding.root.context as Activity).startActivity(
                                intent
                            )
                        }
                    }else{
                        if (!(viewBinding.root.context as Activity).checkPermission(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            )
                        ) {
                            (viewBinding.root.context as Activity).signPermission(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                            return@setOnClickListener
                        }
                    }

                    FileTool.openFilePicker(viewBinding.root.context as Activity,line.supports){
                        if(it!=null){
                            files.add(it)
                            (viewBinding.fileList.adapter as BaseAdapter).notifyDataSetChanged()
                        }
                    }
                }
                ClickAction.Go -> {

                }

            }
        }
    }

    override fun data(): Any? {
        val map= mutableMapOf<String,Any>()
         when(line.format){
            FileFormat.Base64-> {

            }
            FileFormat.Stream-> {
                map[line.serviceName]= files
            }
        }

        return map
    }
    
    
    override fun check(): Boolean {
        return if (line.must){
            files.isNotEmpty()
        }else{
            true
        }

    }

    override fun getTargetView(): View {
        return viewBinding.fileList
    }

    override fun refresh() {
        viewBinding.textTitle.text=  line.title
        viewBinding.textMust.visibility=  if(line.must){
            View.VISIBLE}else{View.INVISIBLE}
        viewBinding.addFile.text="新增${line.title}"
        (viewBinding.fileList.adapter as BaseAdapter).notifyDataSetChanged()
    }

}