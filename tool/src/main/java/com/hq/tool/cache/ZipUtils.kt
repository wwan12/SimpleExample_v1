package com.hq.tool.cache

import android.text.TextUtils

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * 文件描述：文件压缩方法支持目录，单文件压缩解压缩
 * 作者：Administrator
 * 创建时间：2018/8/29/029
 * 更改时间：2018/8/29/029
 * 版本号：1
 */
private val BUFF_SIZE = 1024
/**
 * @param zos           压缩流
 * @param parentDirName 父目录
 * @param file          待压缩文件
 * @param buffer        缓冲区
 * @return 只要目录中有一个文件压缩失败，就停止并返回
 */
private fun zipFile(zos: ZipOutputStream, parentDirName: String, file: File, buffer: ByteArray): Boolean {
    var zipFilePath = parentDirName + file.name
    if (file.isDirectory) {
        zipFilePath += File.separator
        for (f in file.listFiles()) {
            if (!zipFile(zos, zipFilePath, f, buffer)) {
                return false
            }
        }
        return true
    } else {
        try {
            val bis = BufferedInputStream(FileInputStream(file))
            val zipEntry = ZipEntry(zipFilePath)
            zipEntry.size = file.length()
            zos.putNextEntry(zipEntry)
            while (bis.read(buffer) != -1) {
                zos.write(buffer)
            }
            bis.close()
            return true
        } catch (ex: FileNotFoundException) {
            ex.printStackTrace()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return false
    }
}

/**
 * @param srcPath 待压缩的文件或目录
 * @param dstPath 压缩后的zip文件
 * @return 只要待压缩的文件有一个压缩失败就停止压缩并返回(等价于windows上直接进行压缩)
 */
fun File.zipFile(dstPath: String): Boolean {
    if (!this.exists()) {
        return false
    }
    val buffer = ByteArray(BUFF_SIZE)
    try {
        val zos = ZipOutputStream(FileOutputStream(dstPath))
        val result = zipFile(zos, "", this, buffer)
        zos.close()
        return result
    } catch (ex: FileNotFoundException) {
        ex.printStackTrace()
    } catch (ex: IOException) {
        ex.printStackTrace()
    }

    return false
}

/**
 * @param srcPath 待解压的zip文件
 * @param dstPath zip解压后待存放的目录
 * @return 只要解压过程中发生错误，就立即停止并返回(等价于windows上直接进行解压)
 */
fun File.unzipFile(dstPath: String): Boolean {
    var dstPath = dstPath
    if (TextUtils.isEmpty(this.path) || TextUtils.isEmpty(dstPath)) {
        return false
    }
    if (!this.exists() || !this.name.toLowerCase(Locale.getDefault()).endsWith("zip")) {
        return false
    }
    val dstFile = File(dstPath)
    if (!dstFile.exists() || !dstFile.isDirectory) {
        dstFile.mkdirs()
    }
    try {
        val zis = ZipInputStream(FileInputStream(this))
        val bis = BufferedInputStream(zis)
        var zipEntry: ZipEntry? = null
        val buffer = ByteArray(BUFF_SIZE)
        if (!dstPath.endsWith(File.separator)) {
            dstPath += File.separator
        }
        zipEntry = zis.nextEntry
        while (zipEntry != null) {
            val fileName = dstPath + zipEntry?.name
            val file = File(fileName)
            val parentDir = file.parentFile
            if (!parentDir.exists()) {
                parentDir.mkdirs()
            }
            val fos = FileOutputStream(file)
            while (bis.read(buffer) != -1) {
                fos.write(buffer)
            }
            fos.close()
            zipEntry = zis.nextEntry
        }
        bis.close()
        zis.close()
        return true
    } catch (ex: FileNotFoundException) {
        ex.printStackTrace()
    } catch (ex: IOException) {
        ex.printStackTrace()
    }

    return false
}