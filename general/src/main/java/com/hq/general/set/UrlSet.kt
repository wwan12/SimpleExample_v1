package com.hq.general.set

import com.hq.tool.http.Http
import okhttp3.Headers
import okhttp3.Headers.Companion.toHeaders

object UrlSet {



    var BASE_URL=""

    var headers: Headers?=null

    var isDebug:Boolean
    set(value) {
        Http.isDebug=value
    }
    get() {
        return Http.isDebug
    }

    fun setHeader(map:MutableMap<String,String>): Unit {
        headers=map.toHeaders()
    }

}