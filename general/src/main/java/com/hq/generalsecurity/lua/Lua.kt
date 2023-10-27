package com.hq.generalsecurity.lua

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import cn.vimfung.luascriptcore.LuaValue
import cn.vimfung.luascriptcore.LuaContext
import cn.vimfung.luascriptcore.LuaScriptController


object Lua {

    @SuppressLint("StaticFieldLeak")
    var context: LuaContext?=null

    fun init(context: Context): Unit {
        if (this.context!=null){
            return
        }
        //初始化LuaScriptCore
        this.context = LuaContext.create(context)
        //注册一个原生方法给Lua调用
        this.context?.registerMethod("getDeviceInfo") {
            val devInfoMap = HashMap<Any, Any>()
            devInfoMap["deviceName"] = Build.DISPLAY
            devInfoMap["deviceModel"] = Build.MODEL
            devInfoMap["systemName"] = Build.PRODUCT
            devInfoMap["systemVersion"] = Build.VERSION.RELEASE
            LuaValue(devInfoMap)
        }
    }

    fun load(script:String): LuaScriptController {
        //添加控制器
        val controller=LuaScriptController.create()
        //解析Lua脚本

        context?.evalScript(script,controller )
        return  controller
    }

    fun loadFile(filePath:String): LuaScriptController {
        //添加控制器
        val controller=LuaScriptController.create()
        context?.evalScriptFromFile(filePath,controller)
        return  controller
    }

    fun call(name:String,vararg values:Any): Unit {
        //获取变量
       // val luaValue= context?.getGlobal("url")
        //调用方法
        val array= arrayOfNulls<LuaValue>(values.size)
        for (i in values.indices){
            array[i]=LuaValue(values[i])
        }
        context?.callMethod(name, array)
    }
}