package com.hq.generalsecurity.expand

import com.hq.generalsecurity.set.Storage
import com.hq.tool.http.Http
import com.hq.zip.IZipCallback
import com.hq.zip.ZipManager
import java.io.File

object StorageExpand {
    fun downloadResources(path: String,load:(File?)->Unit){
        val ss=path.split("/")
        val oldFile= File("${Storage.downloadPath}/${ss[ss.size-1]}")
        if (oldFile.exists()){
            Storage.pageUpdateTime=oldFile.lastModified()
        }
        Http.download{
            url=path
            downloadPath="${Storage.downloadPath}/${ss[ss.size-1]}"
            success {
                val file = File("${Storage.cachePath}/Page")
                if (Storage.pageUpdateTime!=file.lastModified()){
                    val files = file.listFiles()
                    if (files != null) {
                        for (f in files) {
                            file.delete()
                        }
                    }
                    ZipManager.unzip(downloadPath,"${Storage.cachePath}/Page}",
                        Storage.unZipPwd,object: IZipCallback {
                        override fun onStart() {

                        }

                        override fun onProgress(percentDone: Int) {

                        }

                        override fun onFinish(success: Boolean) {
                            if (success){
                                load(file)
                            }else{
                                load(null)
                            }

                        }

                    })
                }

            }
            fail {
                load(null)
            }
        }
    }

    fun initResources(file: File): Unit {
        val files = file.listFiles()
        if (files != null) {
            for (f in files) {
                Expand.formPage(file.name, file.readText())
            }
        }
    }
    fun getLocalData(name:String): Any {
        return  ""
    }
}