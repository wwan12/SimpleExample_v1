package com.hq.tool.system

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateAndTime {
        //声明一个当前日期时间的属性，
        //返回的日期时间格式形如2017-10-01 10:00:00
        val nowDateTime: String
            //外部访问DateUtil.nowDateTime时，会自动调用nowDateTime附属的get方法得到它的值
            get() {
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    return sdf.format(Date())
                }

        //只返回日期字符串
        val nowDate: String
            get() {
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    return sdf.format(Date())
                }

        //只返回时间字符串
        val nowTime: String
            get() {
                    val sdf = SimpleDateFormat("HH:mm:ss")
                    return sdf.format(Date())
                }

        //返回详细的时间字符串，精确到毫秒
        val nowTimeDetail: String
            get() {
                    val sdf = SimpleDateFormat("HH:mm:ss.SSS")
                    return sdf.format(Date())
                }

        //返回开发者指定格式的日期时间字符串
        fun getFormatTime(format: String=""): String {
                val ft: String = format
                val sdf = if (!ft.isEmpty()) SimpleDateFormat(ft)
                            else SimpleDateFormat("yyyyMMddHHmmss")
                return sdf.format(Date())
            }
    //返回开发者指定格式的日期时间字符串
    fun getFormatTime(date: Date, format: String=""): String {
        val ft: String = format
        val sdf = if (!ft.isEmpty()) SimpleDateFormat(ft)
        else SimpleDateFormat("yyyyMMddHHmmss")
        return sdf.format(date)
    }

 fun changeFormatTime(date:String,format1: String="yyyy-MM-dd HH:mm:ss",format2: String="MMdd"): String {
        val sdf1 = SimpleDateFormat(format1)
        val sdf2 = SimpleDateFormat(format2)
        val timeM: Long = sdf1.parse(date).getTime()
        val tDate= sdf2.format(Date(timeM))
        return tDate
    }

    fun lastLifeTime(call:(TimerTask)->Unit,tick:Long,total:Long): Unit {
        var total=total
        Timer().schedule(object:TimerTask(){
            override fun run() {
                if (total<=0){
                    this.cancel()
                }else{
                    call(this)
                }
                total-=tick
            }
        },tick,total)
    }

    //大于false
    fun less(StartTime: String?, EndTime: String?): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd") //年-月-日 时-分
        try {
            val date1 = dateFormat.parse(StartTime) //开始时间
            val date2 = dateFormat.parse(EndTime) //结束时间
            when {
                date2.time < date1.time -> {
    //                Toast.makeText(PostActivity.this,"结束时间小于开始时间", Toast.LENGTH_SHORT).show();
                    return true
                }
                date2.time == date1.time -> {
    //                Toast.makeText(PostActivity.this,"开始时间与结束时间相同", Toast.LENGTH_SHORT).show();
                    return false
                }
                date2.time > date1.time -> {
                    //正常情况下的逻辑操作.
                    return false
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }

}
