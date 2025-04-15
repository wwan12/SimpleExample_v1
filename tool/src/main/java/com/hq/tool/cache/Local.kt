package com.hq.tool.cache

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.*




object Local {
//    addFileToAlbum(inputStream,filePath,fileName,"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")

    /**添加文件到相册**/
    fun addFileToAlbum(content:Context, fileInput: InputStream, displayName: String, mimeType: String) {
        val values = ContentValues()
        values.put(MediaStore.Downloads.DISPLAY_NAME, displayName)
        values.put(MediaStore.Downloads.MIME_TYPE, mimeType)
        val uri=   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            content.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        } else {
            values.put(MediaStore.MediaColumns.DATA, "${Environment.getExternalStorageDirectory().path}/${Environment.DIRECTORY_DCIM}/$displayName")
            null
        }
//        MediaStore.Downloads.getContentUri("external")
//        val uri = contentResolver.insert(MediaStore.Downloads., values)
        if (uri != null) {
            val outputStream =content. contentResolver.openOutputStream(uri)
            val buffer = ByteArray(2048)
            var len = 0
            while (len != -1) {
                outputStream?.write(buffer, 0, len)
                len = fileInput.read(buffer)
            }
            outputStream?.close()
            fileInput.close()
        }
    }

    /**从静态资源获取文件,并写入新地址**/
    fun getFromAssets(content:Context,filePath: String, fileName: String) {
        var inputStream:InputStream?=null
        try {
            inputStream = content.assets.open(fileName)
            val fileOutputStream = FileOutputStream(File(filePath+fileName))
            val buffer = ByteArray(2048)
            var len = 0
            while (len != -1) {
                fileOutputStream.write(buffer, 0, len)
                len = inputStream.read(buffer)
            }
            fileOutputStream.flush()
            fileOutputStream.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            inputStream?.close()

        }
    }

    fun readAssetTextAsString(context: Context, fileName: String): String {
        val stringBuilder = java.lang.StringBuilder()
        try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
                stringBuilder.append("\n") // 添加换行符
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    fun readAssetsText(context:Context,fileName: String): String  {
        val am = context.assets
        var ips: InputStream? = null
        return try {
            ips = am.open(fileName)
            convertStreamToString(ips)
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        } finally {
            if (ips != null) {
                try {
                    ips.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun convertStreamToString(ips: InputStream): String {
        val reader = BufferedReader(InputStreamReader(ips))
        val sb = StringBuilder()
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                reader.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }
}

