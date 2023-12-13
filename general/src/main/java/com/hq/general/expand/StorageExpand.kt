package com.hq.general.expand

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hq.general.BaseActivity
import com.hq.general.set.Storage
import com.hq.tool.http.Http
import com.hq.tool.loadPro
import com.hq.tool.loge
import com.hq.tool.savePro
import com.hq.zip.IZipCallback
import com.hq.zip.ZipManager
import java.io.File
import java.lang.Exception

object StorageExpand {

    private var caches:MutableMap<String,Any>?= null

    const val SAVE_NAME="SAVE"

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
                Storage.pageUpdateTime=0
                if (Storage.pageUpdateTime!=file.lastModified()){
                    val files = file.listFiles()
                    if (files != null) {
                        for (f in files) {
                            file.delete()
                        }
                    }
                    ZipManager.unzip(downloadPath,"${Storage.cachePath}/Page",
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
        try {
            file.path.loge()
            for (f in files){
                f.path.loge()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

        if (files != null) {
            for (f in files) {
                Expand.formPage(file.name, file.readText())
            }
        }
    }

    fun cache(): Unit {
       val data= Gson().toJson(caches)
        data.savePro(BaseActivity.active.application,SAVE_NAME)
    }

    fun load(): Unit {
       val type=  object :TypeToken<MutableMap<String,Any>>(){}.type
        val save=SAVE_NAME.loadPro(BaseActivity.active.application)
        caches = if (save.isEmpty()){
            mutableMapOf()
        }else{
            Gson().fromJson(save,type)
        }
    }


    fun addLocalData(name:String,value:Any): Unit {
        if (caches==null){
            load()
        }
        caches?.put(name,value)

    }

    fun <T> getLocalData(name:String): T? {
        try {
            if (caches==null){
                load()
            }
            return caches?.get(name) as T
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}