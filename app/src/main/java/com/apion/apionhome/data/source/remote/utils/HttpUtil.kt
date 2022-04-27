package com.apion.apionhome.data.source.remote.utils

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

object HttpUtil {

    fun createPart(name: String, path: String?): MultipartBody.Part? {

        val file: File? = path?.let { File(it) }

        return file?.let {
            val end = file.name.split(".").last()
            val currentTime = Date().time.toString()
            val fileName = "$currentTime.$end"
            val imageRequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            MultipartBody.Part.createFormData(
                name,
                fileName,
                imageRequestBody
            )
        }
    }
}