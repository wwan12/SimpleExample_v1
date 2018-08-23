package com.aisino.tool.system


import android.content.pm.PackageManager
import android.os.Build
import android.content.Context
import com.aisino.tool.log
import java.io.*
import java.util.*

/**
 * <p>文件描述：<p>
 * <p>作者：Administrator<p>
 * <p>创建时间：2018/8/16/016<p>
 * <p>更改时间：2018/8/16/016<p>
 * <p>版本号：1<p>
 *
 */

class CrashHandler
/** 保证只有一个CrashHandler实例  */
private constructor() : Thread.UncaughtExceptionHandler {
    /** 系统默认的UncaughtException处理类  */
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    /** 程序的Context对象  */
    private var mContext: Context? = null
    /** 使用Properties来保存设备的信息和错误堆栈信息 */
    private val mDeviceCrashInfo = Properties()

    private var sendService:(file:File)->Unit= {}

    /**
     * 初始化,注册Context对象,
     * 获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     * @param ctx
     */

    fun init(ctx: Context): CrashHandler? {
        mContext = ctx
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        return INSTANCE
    }
    /**
     * 初始化,设置上传动作
     *
     */
    fun initSend(send:(file:File)->Unit): Unit {
        sendService=send
        sendPreviousReportsToServer()
    }


    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler?.uncaughtException(thread, ex)
        } else {
            //Sleep一会后结束程序
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
//                Log.e(TAG, "Error : ", e)
            }

            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(10)
        }
    }

    /**
     * 自定义错误处理,收集错误信息
     * 发送错误报告等操作均在此完成.
     * 开发者可以根据自己的情况来自定义异常处理逻辑
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            "handleException --- ex==null".log(TAG)
            return true
        }
        ex.localizedMessage ?: return false
//使用Toast来显示异常信息
//        object : Thread() {
//            override fun run() {
//                Looper.prepare()
//                //保存错误报告文件
////                StringToFile(mContext!!).writeToFile(ex.message!!,msg)
////                LogToFile.w("my", msg)
//                //这句话可以先注释掉，这是我单独写的一个log写入类,下面已提供了该类**
//                Looper.loop()
//            }
//        }.start()
        //收集设备信息
        collectCrashDeviceInfo(mContext)
        //保存错误报告文件
        saveCrashInfoToFile(ex);
        //发送错误报告到服务器
        sendCrashReportsToServer(mContext);
        return true
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
        if (crFiles != null && crFiles.size > 0) {
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
        val filter = object : FilenameFilter {
           override fun accept(dir: File, name: String): Boolean {
                return name.endsWith(CRASH_REPORTER_EXTENSION)
            }
        }
        return filesDir.list(filter)
    }

    /**
     * 保存错误信息到文件中
     * @param ex
     * @return
     */
    private fun saveCrashInfoToFile(ex: Throwable): String? {
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
        mDeviceCrashInfo["EXEPTION"] = ex.localizedMessage
        mDeviceCrashInfo[STACK_TRACE] = result
        try {
            val fileName = "crash-${Calendar.YEAR}-${Calendar.WEEK_OF_YEAR}-${Calendar.DAY_OF_MONTH}-${Calendar.HOUR_OF_DAY}:${Calendar.MINUTE}:${Calendar.MILLISECOND}$CRASH_REPORTER_EXTENSION"
            val trace = mContext?.openFileOutput(fileName,
                    Context.MODE_PRIVATE)
            mDeviceCrashInfo.store(trace, "")
            trace?.flush()
            trace?.close()
            return fileName
        } catch (e: Exception) {
            "an error occured while writing report file...".log(TAG)
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

    companion object {

        /** Debug Log tag */
        val TAG = "CrashHandler"
        /** 是否开启日志输出,在Debug状态下开启,
         * 在Release状态下关闭以提示程序性能
         */
        val DEBUG = true
        /** CrashHandler实例  */
        private var INSTANCE: CrashHandler? = null
        private val VERSION_NAME = "versionName"
        private val VERSION_CODE = "versionCode"
        private val STACK_TRACE = "STACK_TRACE"
        /** 错误报告文件的扩展名  */
        private val CRASH_REPORTER_EXTENSION = ".cr"

        private val syncRoot = Any()

        /** 获取CrashHandler实例 ,单例模式 */
        /* if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;*/// 防止多线程访问安全，这里使用了双重锁
        val openCrashHandler: CrashHandler?
            get() {
                if (INSTANCE == null) {
                    synchronized(syncRoot) {
                        if (INSTANCE == null) {
                            INSTANCE = CrashHandler()
                        }
                    }
                }
                return INSTANCE
            }
    }

}