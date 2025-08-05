package com.example.aml.utility

import android.content.Context
import android.net.Uri
import java.io.*

object FileUtil {
    fun from(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = File.createTempFile("upload", ".jpg", context.cacheDir)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
