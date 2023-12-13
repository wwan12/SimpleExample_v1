package com.hq.general.model

import android.app.Activity
import com.hq.general.expand.Expand
import com.hq.general.extraction.DataExtraction
import com.hq.general.widget.file.FileDialog
import com.hq.general.widget.form.Parent

data class FileSet(var format: FileFormat,val supports:ArrayList<String>): LineSet(DataType.File){
    override fun place(activity: Activity, data: DataExtraction): Parent<*, *> {
        val file=FileDialog()
        file.viewBinding = Expand.getViewBinding(activity, file.getViewBindingCls())
        file.line=this
        file.data=data
        file.init()
        return file
    }
}

//enum class FileType(name:String){
//    Zip(".zip"),
//    Doc(".doc"),
//    Docx(".docx"),
//    Wps(".wps"),
//    Xls(".xls"),
//    Xlsx(".xlsx"),
//    Et(".et"),
//    Ppt(".ppt"),
//    Pptx(".pptx"),
//    Dps(".dps"),
//    Pdf(".pdf"),
//    Ofd(".pfd"),
//    Jpg(".jpg"),
//    Png(".png"),
//    Gif(".gif"),
//
//}