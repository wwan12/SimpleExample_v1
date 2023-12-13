package com.hq.general.extraction

import android.view.View
import com.hq.general.expand.StorageExpand
import com.hq.general.model.CacheType
import com.hq.general.model.Option
import com.hq.general.set.Storage
import com.hq.tool.http.SuccessData
import com.hq.tool.loge
import java.lang.Exception

/**
 * 将从后端获取的数据转化为可使用的数据的父类
 */
abstract class DataExtraction(val url:String,val source:CacheType, val serviceName:String) {

    var data:Any?=null

    abstract fun  fromData(netData: SuccessData?): Unit


    fun <E> getDefData(data: SuccessData?): E? {
        if (serviceName.isEmpty()) {
            return null
        }
//        if (data!=null&&source!=CacheType.Local){
//           val e= data.tryGet<E>(serviceName)
//            if (e!=null){
//                return e
//            }
//        }
        return when(source){
            CacheType.Local-> {
                try {
                    val ld = StorageExpand.getLocalData<ArrayList<Option>>(serviceName)
                    if (ld != null) {
                        for (o in ld) {
                            "${o.id}->${data?.tryGet<String>(serviceName)}".loge("options")
                            if (o.id == data?.tryGet<String>(serviceName)) {
                                return o.title as E?
                            }
                        }
                    }
                   "${serviceName}:${ld}".loge("Local")
                }catch (e:Exception){
                    "${serviceName}:${StorageExpand.getLocalData(serviceName) as E?}".loge("Local")
//                    e.printStackTrace()
                }

                StorageExpand.getLocalData(serviceName) as E?
            }
            CacheType.Net-> {
                "${serviceName}:${data?.tryGet<E>(serviceName)}".loge("Net")
                data?.tryGet<E>(serviceName)
            }
            else->null
        }
    }
}