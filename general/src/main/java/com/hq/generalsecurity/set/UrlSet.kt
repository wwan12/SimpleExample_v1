package com.hq.generalsecurity.set

import okhttp3.Headers
import okhttp3.Headers.Companion.toHeaders

object UrlSet {



    var BASE_URL=""

    var headers: Headers?=null

    fun setHeader(map:MutableMap<String,String>): Unit {
        headers=map.toHeaders()
    }
}