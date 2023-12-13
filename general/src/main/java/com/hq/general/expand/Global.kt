package com.hq.general.expand

import com.hq.general.extraction.DataExtraction
import com.hq.general.extraction.ImgExtraction
import com.hq.general.extraction.OnlyDataExtraction
import com.hq.general.extraction.TextExtraction
import com.hq.general.model.CacheType
import com.hq.general.model.DataType

object Global {

    private val dataExtractionList= mutableListOf<DataExtraction>()

    fun addDataExtraction(dataExtraction: DataExtraction): Unit {
        dataExtractionList.add(dataExtraction)
    }

    fun getDataExtraction(url:String,source:CacheType,serviceName:String,type:DataType): DataExtraction {
       // val list= arrayListOf<DataExtraction>()
        for (data in dataExtractionList){
            if ((data.url.isEmpty()||data.url==url)&&data.serviceName==serviceName){
                return data
            }
        }
       return when(type){
            DataType.OnlyData-> OnlyDataExtraction(url,source,serviceName)
            DataType.Text-> TextExtraction(url,source,serviceName)
            DataType.Img-> ImgExtraction(url,source,serviceName)
            DataType.Group-> TextExtraction(url,source,serviceName)
            DataType.File-> OnlyDataExtraction(url,source,serviceName)
        }
    }
}