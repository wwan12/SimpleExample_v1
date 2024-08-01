package com.hq.tool.http

class FailData(val url:String,val code:Int, note: String) {
    var submitTime=""
    var failMsg=""
    init {
        failMsg=note
    }
}