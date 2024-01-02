package com.hq.general.model

import com.hq.tool.file.FileTool
import java.io.File

class FileShell (var file: File?=null,var url:String?=null){

    private var mSize:String="0KB"

    var size:String
    get() {
       return  mSize
    }
    set(value) {
        mSize=value
    }

    private var mName:String="0KB"

    var name:String
        get() {
            return mName
        }
        set(value) {
            mName=value
            if (file!=null){
                val oleFile = File(file!!.path+"/"+file!!.name)
                val newFile = File(file!!.path+"/"+mName)
//执行重命名
                oleFile.renameTo(newFile)
            }
        }


    init {
        if (file!=null){
            mName=file!!.name
            mSize= FileTool.formatKMGByBytes(file!!.length())
        }
        if (url!=null){

        }
    }
}