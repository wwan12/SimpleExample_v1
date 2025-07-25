package com.hq.tool.http

import com.google.gson.GsonBuilder
import com.hq.tool.http.gson.MyTypeAdapterFactory
import java.lang.Exception

/**
 * 文件描述：
 * 作者：Administrator
 * 创建时间：2018/9/19/019
 * 更改时间：2018/9/19/019
 * 版本号：1
 *
 */
class SuccessData (url:String,data: MutableMap<String,Any>){
    val params=mutableMapOf<String,Any>()
    val data= mutableMapOf<String,Any>()
    var url=""
    var submitTime=""
    var retryCount=0
    var stringBody=""
    var code=0
    init {
        this.url=url
        this.data.putAll(data)
        if (data.containsKey(ReturnType.STRING.name)){
            stringBody=data[ReturnType.STRING.name].toString()
        }
    }

    fun logParams(): Unit {
        
    }

    fun logCallBack(): Unit {
        
    }

    fun logAll(): Unit {
        
    }

    inline fun <reified E>getEntity (): E {
     val gson= GsonBuilder().registerTypeAdapterFactory(MyTypeAdapterFactory()).create()

       return gson.fromJson<E>(stringBody,E::class.java)
    }

    
    operator fun <E>get(key:String): E{
        return loopAny<E>(key, data) as E
    }
     fun <E>tryGet(key:String): E?{
         return try {
             loopAny<E>(key, data)
         }catch (e:Exception){
             e.printStackTrace()
             null
         }
    }

    private fun <E> loopAny(key: String, target: MutableMap<String, Any>): E? {
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