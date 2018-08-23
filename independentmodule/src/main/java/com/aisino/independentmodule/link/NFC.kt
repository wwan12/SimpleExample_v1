package com.aisino.independentmodule.link

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.content.IntentFilter
import android.app.PendingIntent
import android.nfc.NfcAdapter
import android.widget.Toast
import android.nfc.tech.Ndef
import android.nfc.tech.MifareUltralight
import android.nfc.tech.NfcA
import android.content.Intent
import android.graphics.Color
import android.nfc.Tag
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.FormatException
import com.aisino.independentmodule.R
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


/**
 * Created by lenovo on 2017/12/11.
 */
//val NFC_LAYOUT = "NFC_LAYOUT"
val NFC_MSG = "NFC_MSG"
val NFC_MODLE = "NFC_MODLE"
val NFC_READ = "NFC_READ"
val NFC_WRITE = "NFC_WRITE"
val NFC_DELETE = "NFC_DELETE"

//权限
//   <uses-permission android:name="android.permission.NFC" />
//<uses-feature
//android:name="android.hardware.nfc"
//android:required="true" />

class NFC (activity: AppCompatActivity,mode:String ,msg:String ) {
    // NFC适配器
    private var nfcAdapter: NfcAdapter? = null
    // 传达意图
    private var pi: PendingIntent? = null
    // 滤掉组件无法响应和处理的Intent
    private var tagDetected: IntentFilter? = null
    // 文本控件
//    private val nfc_text: TextView? = null
    // 是否支持NFC功能的标签
    private var isNFC_support = false

    private var activity:AppCompatActivity
    private var msg:String

    var msgListenr={msg:String->}

    init {
        this.activity=activity
        this.msg= msg
        initNFCData()
    }


    //返回一个NdefRecord实例
    @Throws(UnsupportedEncodingException::class)
    private fun createRecord(): NdefRecord {
        //组装字符串，准备好你要写入的信息
        //将字符串转换成字节数组
        val textBytes = msg?.toByteArray()
        //将字节数组封装到一个NdefRecord实例中去
        return NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                "text/x-vCard".toByteArray(), byteArrayOf(), textBytes)
    }

    fun onNfcResume() {
        if (isNFC_support == false) {
            // 如果设备不支持NFC或者NFC功能没开启，就return掉
            return
        }
        // 开始监听NFC设备是否连接
        startNFC_Listener()

//        if (NfcAdapter.ACTION_TECH_DISCOVERED == this.intent.action) {
//            // 注意这个if中的代码几乎不会进来，因为刚刚在上一行代码开启了监听NFC连接，下一行代码马上就收到了NFC连接的intent，这种几率很小
//            // 处理该intent
//            processIntent(this.intent)
//        }
    }

     fun onNfcPause() {
        if (isNFC_support == true) {
            // 当前Activity如果不在手机的最前端，就停止NFC设备连接的监听
            stopNFC_Listener()
        }
    }

    fun onNfcMsg(msg: String): Unit {

    }

    private fun initNFCData() {
        // 初始化设备支持NFC功能
        isNFC_support = true
        // 得到默认nfc适配器
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
        // 提示信息定义
        var metaInfo = ""
        // 判定设备是否支持NFC或启动NFC
        if (nfcAdapter == null) {
            metaInfo = "设备不支持NFC！"
            Toast.makeText(activity, metaInfo, Toast.LENGTH_SHORT).show()
            isNFC_support = false
            return
        }
        if (!nfcAdapter?.isEnabled!!) {
            metaInfo = "请在系统设置中先启用NFC功能！"
//            Toast.makeText(activity, metaInfo, Toast.LENGTH_SHORT).show()
            isNFC_support = false
        }

        if (isNFC_support == true) {
            init_NFC()
        } else {
            msgListenr(metaInfo)
        }
    }

    // 字符序列转换为16进制字符串
    private fun bytesToHexString(src: ByteArray): String? {
        return bytesToHexString(src, true)
    }

    private fun bytesToHexString(src: ByteArray?, isPrefix: Boolean): String? {
        val stringBuilder = StringBuilder()
        if (isPrefix == true) {
            stringBuilder.append("0x")
        }
        if (src == null || src.size <= 0) {
            return null
        }
        val buffer = CharArray(2)
        for (i in src.indices) {
            buffer[0] = Character.toUpperCase(Character.forDigit(
                    src[i].toInt().ushr(4) and 0x0F, 16))
            buffer[1] = Character.toUpperCase(Character.forDigit(src[i].toInt() and 0x0F,
                    16))
            println(buffer)
            stringBuilder.append(buffer)
        }
        return stringBuilder.toString()
    }

    private var tagFromIntent: Tag? = null

    /**
     * Parses the NDEF Message from the intent and prints to the nfc_text
     */
    fun processIntent(intent: Intent) {
        if (isNFC_support == false)
            return

        // 取出封装在intent中的TAG
        tagFromIntent = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)

//        nfc_text?.setTextColor(Color.BLUE)
        var metaInfo = ""
        metaInfo += "卡片ID：" + bytesToHexString(tagFromIntent!!.getId()) + "\n"
//        Toast.makeText(this, "找到卡片", Toast.LENGTH_SHORT).show()
        msgListenr(metaInfo)

        // Tech List
        val prefix = "android.nfc.tech."
        val techList = tagFromIntent!!.getTechList()

        //分析NFC卡的类型： Mifare Classic/UltraLight Info
        var CardType = ""
        for (i in techList.indices) {
            if (techList[i] == NfcA::class.java.name) {
                // 读取TAG
                val mfc = NfcA.get(tagFromIntent)
                try {
                    if ("" == CardType)
                        CardType = "MifareClassic卡片类型 \n 不支持NDEF消息 \n"
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else if (techList[i] == MifareUltralight::class.java.name) {
                val mifareUlTag = MifareUltralight
                        .get(tagFromIntent)
                var lightType = ""
                // Type Info
                when (mifareUlTag.type) {
                    MifareUltralight.TYPE_ULTRALIGHT -> lightType = "Ultralight"
                    MifareUltralight.TYPE_ULTRALIGHT_C -> lightType = "Ultralight C"
                }
                CardType = lightType + "卡片类型\n"

                val ndef = Ndef.get(tagFromIntent)
                CardType += "最大数据尺寸:" + ndef.maxSize + "\n"

            }
        }
        metaInfo += CardType
        msgListenr(metaInfo)
//        nfc_text?.setText(metaInfo)
        when (intent.extras?.getString(NFC_MODLE)) {
            NFC_READ -> {
                val nfcText = read(tagFromIntent)
                msgListenr(nfcText!!)
            }
            NFC_WRITE -> write(tagFromIntent)
            NFC_DELETE -> delete(tagFromIntent)
        }
    }

    // 读取方法
    @SuppressLint("MissingPermission")
    @Throws(IOException::class, FormatException::class)
    private fun read(tag: Tag?): String? {
        if (tag != null) {
            //解析Tag获取到NDEF实例
            val ndef = Ndef.get(tag)
            //打开连接
            ndef.connect()
            //获取NDEF消息
            val message = ndef.ndefMessage
            //将消息转换成字节数组
            val data = message.toByteArray()
            //将字节数组转换成字符串
            val str = String(data, Charset.forName("UTF-8"))
            //关闭连接
            ndef.close()
            return str
        } else {

            Toast.makeText(activity, "设备与nfc卡连接断开，请重新连接...",
                    Toast.LENGTH_SHORT).show()
        }
        return null
    }

    // 写入方法
    @SuppressLint("MissingPermission")
    @Throws(IOException::class, FormatException::class)
    private fun write(tag: Tag?) {
        if (tag != null) {
            //新建NdefRecord数组，本例中数组只有一个元素
            val records = arrayOf<NdefRecord>(createRecord())
            //新建一个NdefMessage实例
            val message = NdefMessage(records)
            // 解析TAG获取到NDEF实例
            val ndef = Ndef.get(tag)
            // 打开连接
            ndef.connect()
            // 写入NDEF信息
            ndef.writeNdefMessage(message)
            // 关闭连接
            ndef.close()
//            nfc_text?.setText(nfc_text.getText().toString() + "写入数据成功！" + "\n")
            Toast.makeText(activity, "写入数据成功！",
                    Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "设备与nfc卡连接断开，请重新连接...",
                    Toast.LENGTH_SHORT).show()
        }
    }

    // 删除方法
    @SuppressLint("MissingPermission")
    @Throws(IOException::class, FormatException::class)
    private fun delete(tag: Tag?) {
        if (tag != null) {
            //新建一个里面无任何信息的NdefRecord实例
            val nullNdefRecord = NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                    byteArrayOf(), byteArrayOf(), byteArrayOf())
            val records = arrayOf(nullNdefRecord)
            val message = NdefMessage(records)
            // 解析TAG获取到NDEF实例
            val ndef = Ndef.get(tag)
            // 打开连接
            ndef.connect()
            // 写入信息
            ndef.writeNdefMessage(message)
            // 关闭连接
            ndef.close()
//            nfc_text?.setText(nfc_text.getText().toString() + "删除数据成功！" + "\n")
            Toast.makeText(activity, "删除数据成功",
                    Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "设备与nfc卡连接断开，请重新连接...",
                    Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startNFC_Listener() {
        // 开始监听NFC设备是否连接，如果连接就发pi意图
        nfcAdapter?.enableForegroundDispatch(activity, pi, arrayOf<IntentFilter>(tagDetected!!), null)
    }

    @SuppressLint("MissingPermission")
    private fun stopNFC_Listener() {
        // 停止监听NFC设备是否连接
        nfcAdapter?.disableForegroundDispatch(activity)
    }

    private fun init_NFC() {
        // 初始化PendingIntent，当有NFC设备连接上的时候，就交给当前Activity处理
        pi = PendingIntent.getActivity(activity, 0, Intent(activity, javaClass)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        // 新建IntentFilter，使用的是第二种的过滤机制
        tagDetected = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        tagDetected?.addCategory(Intent.CATEGORY_DEFAULT)

    }
}