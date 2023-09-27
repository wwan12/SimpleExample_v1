package com.hq.tool.http

class FailData(val url:String, note: String) {
    var submitTime=""
    var failMsg=""
    init {
        failMsg=note
    }
}