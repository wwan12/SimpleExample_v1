package com.hq.general.lua

import cn.vimfung.luascriptcore.LuaExportType
import cn.vimfung.luascriptcore.LuaFunction
import cn.vimfung.luascriptcore.LuaValue
import com.hq.general.BaseActivity
import com.hq.general.set.UrlSet
import com.hq.tool.http.Http
import com.hq.tool.toast

class LuaHttpTool: LuaExportType {
    fun get(mUrl:String,result: LuaFunction, fault:LuaFunction): Unit {
        Http.get{
            url=UrlSet.BASE_URL+mUrl
            success {

            }
            fail {
                when (fault(arrayOf(LuaValue(-1))).toInteger()) {
                    0L->"网络错误".toast(com.hq.general.BaseActivity.active)
                }
            }
        }
    }

    fun post(result: LuaFunction, fault:LuaFunction): Unit {
        
    }
}