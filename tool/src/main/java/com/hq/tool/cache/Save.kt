import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.hq.tool.dialog
import com.hq.tool.toast
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

open class Save {
//    addFileToAlbum(inputStream,filePath,fileName,"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")

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
}

