package com.aisino.tool.http

import com.aisino.tool.loge
import com.google.gson.Gson

/**
 * 文件描述：
 * 作者：Administrator
 * 创建时间：2018/9/19/019
 * 更改时间：2018/9/19/019
 * 版本号：1
 *
 */
class SuccessData (url:String,params: MutableMap<String,Any>,data: MutableMap<String,Any>){
    var params:MutableMap<String,Any>?=null
    var data:MutableMap<String,Any>?=null
    var url=""
    var submitTime=""
    var gson:Gson?=null
    var retryCount=0
    init {
        this.url=url
        this.data =data
        this.params=params
    }

    fun logParams(): Unit {
        
    }

    fun logCallBack(): Unit {
        
    }

    fun logAll(): Unit {
        "param:${params}".loge(url)
        "data:${data}".loge(url)
    }
    inline fun <reified E>getForObj(key:String): E?{
        val value= loopAny<E>(key, data!!)
        if (value!=null){
           return gson?.fromJson<E>(value.toString(),E::class.java)
        }else{
            return null
        }
    }

    operator fun <E>get(key:String): E{
        return loopAny<E>(key, data!!) as E
    }

     fun <E> loopAny(key: String, target: MutableMap<String, Any>): E? {
        var result: E?
        if (target.containsKey(key)) {
            return target[key] as E
        }//找到了就直接返回
        for (res in target) {
            if (res.value is MutableMap<*, *>) {
                result = loopAny<E>(key, res.value as MutableMap<String, Any>)
                if (result != null) {
                    return result
                }
            }
        }

        return null
    }
    
}