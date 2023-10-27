package com.hq.tool.system

import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object WaitTool {

    fun waitForSecond(time: Long,call:()->Unit): Job {
       return MainScope().launch {
            delay(time)
            call()
        }
    }

    fun waitForLoop(time: Long,call:(Int)->Unit): Job {
        var i=0
       return MainScope().launch {
           while (true){
               delay(time)
               i++
               call(i)
           }
        }
    }
}