package com.hq.tool.system

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.hq.tool.log
import java.io.File
import java.io.FilenameFilter
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception
import java.util.*
/** 全局异常捕获 */
class CrashLoop {
    /** 程序的Context对象  */
    private var mContext: Context? = null
    /** 使用Properties来保存设备的信息和错误堆栈信息 */
    private val mDeviceCrashInfo = Properties()

    private var sendService:(file: File)->Unit= {}
    private val VERSION_NAME = "versionName"
    private val VERSION_CODE = "versionCode"
    private val STACK_TRACE = "STACK_TRACE"
    /** 错误报告文件的扩展名  */
    private val CRASH_REPORTER_EXTENSION = ".cr"

    val TAG="CrashLoop"

    var crashCallBack:(e:Throwable)->Unit={}

    companion object{
        var crashLoop:CrashLoop?=null
    }

    fun init(mContext: Context): CrashLoop {
        this.mContext=mContext
        crashLoop=this
        Handler(Looper.getMainLooper()).post {
            while (true){
                try {
                    Looper.loop()
                }catch (e:Exception) {
                    e.printStackTrace()
                    catchException(e)
                }

            }
        }
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            catchException(throwable)
            thread.stop()
        }
        return this
    }
    /**
     *
     */
    fun catchException(throwable: Throwable): Unit {
        //收集设备信息
        collectCrashDeviceInfo(mContext)
        //保存错误报告文件
        saveCrashInfoToFile(throwable)
        //发送错误报告到服务器
     //   sendCrashReportsToServer(mContext)

        crashCallBack(throwable)
    }

    /**
     * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
     */
    fun sendPreviousReportsToServer() {
        sendCrashReportsToServer(mContext)
    }

    /**
     * 把错误报告发送给服务器,包含新产生的和以前没发送的.
     * @param ctx
     */
    private fun sendCrashReportsToServer(ctx: Context?) {
        val crFiles = getCrashReportFiles(ctx!!)
        if (crFiles != null && crFiles.isNotEmpty()) {
            val sortedFiles = TreeSet<String>()
            sortedFiles.addAll(crFiles)
            for (fileName in sortedFiles) {
                val cr = File(ctx.filesDir, fileName)
                postReport(cr)
                cr.delete()// 删除已发送的报告
            }
        }
    }

    private fun postReport(file: File) {
        sendService(file)
    }

    /**
     * 获取错误报告文件名
     * @param ctx
     * @return
     */
    private fun getCrashReportFiles(ctx: Context): Array<String>? {
        val filesDir = ctx.filesDir
        val filter = FilenameFilter { dir, name -> name.endsWith(CRASH_REPORTER_EXTENSION) }
        return filesDir.list(filter)
    }

    /**
     * 保存错误信息到文件中
     * @param ex
     * @return
     */
    private fun saveCrashInfoToFile(ex: Throwable): String? {
        try {
        val info = StringWriter()
        val printWriter = PrintWriter(info)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        val result = info.toString()
        printWriter.close()

        mDeviceCrashInfo["EXEPTION"] =  if (ex.localizedMessage==null){
            "空localizedMessage消息"
        }else{
            ex.localizedMessage
        }
        mDeviceCrashInfo[STACK_TRACE] = result
            val fileName = "crash-${Calendar.YEAR}-${Calendar.WEEK_OF_YEAR}-${Calendar.DAY_OF_MONTH}-${Calendar.HOUR_OF_DAY}:${Calendar.MINUTE}:${Calendar.MILLISECOND}${CRASH_REPORTER_EXTENSION}"
            val trace = mContext?.openFileOutput(fileName,
                    Context.MODE_PRIVATE)
            mDeviceCrashInfo.store(trace, "")
            trace?.flush()
            trace?.close()
            return fileName
        } catch (e: Exception) {
           e.printStackTrace()
        }

        return null
    }

    /**
     * 收集程序崩溃的设备信息
     *
     * @param ctx
     */
    fun collectCrashDeviceInfo(ctx: Context?) {
        try {
            val pm = ctx?.packageManager
            val pi = pm?.getPackageInfo(ctx.packageName,
                    PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                mDeviceCrashInfo[VERSION_NAME] = if (pi.versionName == null) "not set" else pi.versionName
                mDeviceCrashInfo[VERSION_CODE] = "" + pi.versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            "Error while collect package info".log(TAG)
        }

        //使用反射来收集设备信息.在Build类中包含各种设备信息,
        //例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        //具体信息请参考后面的截图
        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                mDeviceCrashInfo[field.name] = "" + field.get(null)
                (field.name + " : " + field.get(null)).log(TAG)
            } catch (e: Exception) {
                "Error while collect crash info".log(TAG)
            }

        }
    }
}