package com.hq.tool.http

import android.util.Log
import com.hq.tool.log
import com.hq.tool.loge
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit


fun get(url:String,headers: Headers,_params: Map<String, Any>,kCall: KCall): Unit {
    val okHttpClient = OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
    var cacheUrl=url
    if (cacheUrl.isNotEmpty() && cacheUrl.substring(cacheUrl.length - 1, cacheUrl.length) != "?" && _params.isNotEmpty()) {
        cacheUrl += "?"
    }
    for (p in _params) {
        cacheUrl = cacheUrl + p.key + "=" + p.value + "&"
    }
    if (_params.isNotEmpty()) {
        cacheUrl = cacheUrl.substring(0, cacheUrl.length - 1)
    }
    "DEBUGAPI->$cacheUrl".loge("api")
    val request = Request.Builder().headers(headers).url(cacheUrl).build()
    val call = okHttpClient.build().newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            kCall.failCall(e.toString())
        }

        override fun onResponse(call: Call, response: Response) {
            response.request.url.toString().log("successCall")
            var jsonString = response.body?.string()
            jsonString?.log("successCall")
            kCall.successCall(jsonString!!)
        }
    })
}

 fun post(url:String,headers: Headers,_params: Map<String, Any>,kCall: KCall): Unit {
    val okHttpClient = OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
    val build = FormBody.Builder()
    url.log("post")
    for (p in _params) {
        build.add(p.key, p.value.toString())
        (p.key + "-" + p.value.toString()).log("post")
    }
    val body = build.build()
    val request = Request.Builder().headers(headers).url(url).post(body).build()
    val call = okHttpClient.build().newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            kCall.failCall(e.toString())

        }

        override fun onResponse(call: Call, response: Response) {
            response.request.url.toString().log("successCall")
            var jsonString = response.body?.string()
            jsonString?.log("successCall")
            kCall.successCall(jsonString!!)
        }
    })
}

fun postJson(url: String?,headers: Headers, json: String?, kCall: KCall) {
    val okHttpClient = OkHttpClient.Builder()
    okHttpClient.connectTimeout(5, TimeUnit.SECONDS)
       //     "".toRequestBody("application/json".toMediaTypeOrNull())
    val build = RequestBody.create("application/json".toMediaTypeOrNull(), json!!)
    Log.e("up", json)
    val request = Request.Builder().headers(headers).url(url!!).post(build).build()
    val call = okHttpClient.build().newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            kCall.failCall(e.toString())
        }

        override fun onResponse(call: Call, response: Response) {
            val json = response.body!!.string()
                Log.e("call", json)
                kCall.successCall(json)

        }
    })
}


interface KCall {
    fun successCall(json:String)
    fun failCall(msg:String)
}
