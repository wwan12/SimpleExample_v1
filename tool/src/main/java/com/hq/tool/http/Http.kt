package com.hq.tool.http

/**
 * Created by lenovo on 2017/11/14.
 *
 * Http.get {
 *url = "login"
 *"kotlin"-"1.6"//添加参数
 *请求开始
 *start {  }
 *请求成功
 *success {
 *getAny<ArrayList<String>>("ids")//取出集合ids
 *!"id"//取出STRING参数id
 *"user".."id"//取出userJOBJ对象中STRING参数id
 *}
 *请求失败
 *fail { failMsg -> Toast.makeText(this@NetWorkActivity, failMsg, Toast.LENGTH_SHORT).show() }
 *}
 */
object Http {

    var isDebug=false

    var any = fun(method:Method,function: Submit.() -> Unit) {
        val sub = Submit()
        sub.method = method
        sub.function()
        sub.run()

    }
    var get = fun(function: Submit.() -> Unit) {
        val sub = Submit()
        sub.method = Method.GET
        sub.function()
        sub.run()

    }

    var post = fun(function: Submit.() -> Unit) {
        val sub = Submit()
        sub.method = Method.POST
        sub.function()
        sub.run()
    }

    var postjson = fun(function: Submit.() -> Unit) {
        val sub = Submit()
        sub.method = Method.POSTJSON
        sub.function()
        sub.run()
    }

    var upimage = fun(function: Submit.() -> Unit) {
        val sub = Submit()
        sub.function()
        sub.method = Method.IMAGE
        sub.run()
    }

    var upfile = fun(function: Submit.() -> Unit) {
        val sub = Submit()
        sub.function()
        sub.method = Method.FILE
        sub.run()
    }

    var download = fun(function: Submit.() -> Unit) {
        val sub = Submit()
        sub.function()
        sub.method = Method.DOWNLOAD
        sub.returnType = ReturnType.FILE
        sub.run()
    }
    var put = fun(function: Submit.() -> Unit) {
        val sub = Submit()
        sub.function()
        sub.method = Method.PUT
        sub.run()
    }

    var putjson = fun(function: Submit.() -> Unit) {
        val sub = Submit()
        sub.function()
        sub.method = Method.PUTJSON
        sub.run()
    }
    var stringJson = fun(function: Submit.() -> Unit) {
        val sub = Submit()
        sub.function()
        sub.method = Method.STRING
        sub.run()
    }

    var test = fun(function: HashMap<String,Submit.TestResult>.() -> Unit) {
        val h = HashMap<String,Submit.TestResult>()
        h.function()
        testResult.putAll(h)
    }
    var testJson = fun(function: HashMap<String,String>.() -> Unit) {
        val h = HashMap<String,String>()
        h.function()
        for (i in h){
            testResult[i.key] = Submit.TestResult(200,i.key,i.value,500)
        }
    }
    var socketOpen = fun(function: Submit.() -> Unit) {
        val sub = Submit()
        sub.function()
        sub.method = Method.SOCKET
        sub.returnType=ReturnType.STRING
        sub.run()
    }


    fun addInterceptor(interceptor:Interceptor): Unit {
        interceptors.add(interceptor)
    }

    fun removeInterceptor(interceptor:Interceptor?=null): Unit {
        if (interceptor!=null){
            interceptors.remove(interceptor)
        }else{
            interceptors.clear()
        }
    }
}

