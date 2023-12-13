package com.hq.general.debug

import com.google.gson.Gson
import com.hq.general.model.FormStandardPage
import com.hq.general.model.ListStandardPage
import kotlin.random.Random

object TestDataCreator {
    fun createListData(listPage: ListStandardPage): String {
        val r = Random(System.currentTimeMillis())
        val totalData= mutableMapOf<String,Any>()
        val list = arrayListOf<MutableMap<String, String>>()
        for (i in 0 until r.nextInt(20, 30)) {
            val map = mutableMapOf<String, String>()
            for (line in listPage.lineSets) {
                if (!map.containsKey(line.serviceName)){
                    map[line.serviceName]="${line.serviceName}:测试文本"
                }
            }
            list.add(map)
        }
        totalData[listPage.rowName]=list
        return Gson().toJson(totalData)
    }

    fun createFromData(formPage: FormStandardPage): String {
        val map = mutableMapOf<String, String>()
        for (line in formPage.lineSets) {
            if (!map.containsKey(line.serviceName)){
                map[line.serviceName]="${line.serviceName}:测试文本"
            }
        }
        return Gson().toJson(map)
    }
}