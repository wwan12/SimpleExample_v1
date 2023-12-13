package com.hq.general.extraction

import com.hq.general.model.CacheType
import com.hq.tool.http.SuccessData
import com.hq.tool.toStringPro

class ImgExtraction(url:String, source: CacheType, serviceName: String) : DataExtraction(url,source, serviceName) {
    override fun fromData(netData: SuccessData?): Unit {
        data= getDefData<String>(netData).toStringPro()
    }
}