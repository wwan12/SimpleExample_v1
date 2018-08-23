package com.aisino.tool.http

import com.aisino.tool.DEBUG

/**
 * Created by lenovo on 2017/11/14.
 */
object Http {

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
        sub.run()
    }

}

