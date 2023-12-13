package com.hq.tool.http

interface Interceptor {

     fun check(url: String): Boolean

     fun outPut(params:MutableMap<String,Any>): Unit

     fun inPut(data:String): String
}