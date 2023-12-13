package com.hq.general.lua

import cn.vimfung.luascriptcore.LuaExportType
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * 加密类
 */
object LuaCrypto : LuaExportType {
    /**
     * 对数据使用MD5
     * @param data 需要MD5的数据
     * @return MD5后的数据
     */
    fun md5(data: Any?): ByteArray? {
        if (data is ByteArray || data is String) {
            var rawData: ByteArray? = null
            rawData = if (data is String) {
                data.toByteArray()
            } else {
                data as ByteArray?
            }
            try {
                val md = MessageDigest.getInstance("MD5")
                md.update(rawData)
                return md.digest()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 对数据使用SHA1
     * @param data 需要SHA1的数据
     * @return SHA1后的数据
     */
    fun sha1(data: Any?): ByteArray? {
        if (data is ByteArray || data is String) {
            var rawData: ByteArray? = null
            rawData = if (data is String) {
                data.toByteArray()
            } else {
                data as ByteArray?
            }
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(rawData)
                return md.digest()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 对数据使用HMAC-MD5
     *
     * @param data 需要HMAC-MD5的数据
     * @param key 密钥
     * @return HMAC-MD5后的数据
     */
    fun hmacMD5(data: Any?, key: Any?): ByteArray? {
        var rawKeyData: ByteArray? = null
        var rawData: ByteArray? = null
        if (key is ByteArray || key is String) {
            rawKeyData = if (key is String) {
                key.toByteArray()
            } else {
                key as ByteArray?
            }
        }
        if (data is ByteArray || data is String) {
            rawData = if (data is String) {
                data.toByteArray()
            } else {
                data as ByteArray?
            }
        }
        if (rawKeyData != null && rawData != null) {
            try {
                val mac = Mac.getInstance("HmacMD5")
                val secret = SecretKeySpec(
                    rawKeyData, "HmacMD5"
                )
                mac.init(secret)
                return mac.doFinal(rawData)
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 对数据使用HMAC-SHA1
     * @param key 密钥
     * @param data 需要HMAC-SHA1的数据
     * @return HMAC-SHA1后的数据
     */
    fun hmacSHA1(data: Any?, key: Any?): ByteArray? {
        var rawKeyData: ByteArray? = null
        var rawData: ByteArray? = null
        if (key is ByteArray || key is String) {
            rawKeyData = if (key is String) {
                key.toByteArray()
            } else {
                key as ByteArray?
            }
        }
        if (data is ByteArray || data is String) {
            rawData = if (data is String) {
                data.toByteArray()
            } else {
                data as ByteArray?
            }
        }
        if (rawKeyData != null && rawData != null) {
            try {
                val mac = Mac.getInstance("HmacSHA1")
                val secret = SecretKeySpec(
                    rawKeyData, "HmacSHA1"
                )
                mac.init(secret)
                return mac.doFinal(rawData)
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
        }
        return null
    }
}