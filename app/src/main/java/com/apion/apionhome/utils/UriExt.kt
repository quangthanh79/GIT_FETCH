package com.apion.apionhome.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.lang.Exception

fun Uri.getRealPath(context: Context): String {
    val sdkVersion = Build.VERSION.SDK_INT
    return if (sdkVersion >= 19) { // api >= 19
        getRealPathFromUriAboveApi19(context, this)
    } else { // api < 19
        getRealPathFromUriBelowAPI19(context, this)
    }
}


/**
 * Adaptation api19 less (not including api19), uri get the absolute path based on the picture
 *
 * @Param context context object
 * @Param uri pictures of Uri
 * @Return if Uri corresponding images exist, then returns the absolute pathname of the picture, otherwise null
 */
private fun getRealPathFromUriBelowAPI19(context: Context, uri: Uri): String {
    return getDataColumn(context, uri, null, null)
}

/**
 * Adaptation api19 and above, to obtain images in accordance with the absolute path uri
 *
 * @Param context context object
 * @Param uri pictures of Uri
 * @Return if Uri corresponding images exist, then returns the absolute pathname of the picture, otherwise null
 */
private fun getRealPathFromUriAboveApi19(context: Context, uri: Uri): String {
    var filePath = ""
    if (DocumentsContract.isDocumentUri(context, uri)) {
        // If this is the type of document uri, then be handled by the document id
        val documentId = DocumentsContract.getDocumentId(uri)
        if (isMediaDocument(uri)) { // MediaProvider
            val id = documentId.split(":")[1]

            val selection = MediaStore.Images.Media._ID + "=?"

            val selectionArgs = arrayOf(id)

            filePath = getDataColumn(
                context,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                selection,
                selectionArgs
            )
        } else if (isDownloadsDocument(uri)) { // DownloadsProvider
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                documentId.toLong()
            )
            filePath = getDataColumn(context, contentUri, null, null)
        }
    } else if ("content".equals(uri.scheme, true)) {
        filePath = getDataColumn(context, uri, null, null)
    } else if ("file" == uri.scheme) {
        // If the file is a type of Uri, direct access path corresponding picture
        filePath = uri.path ?: ""
    }
    return filePath
}

/**
 * Get _data columns in a database table, i.e., returns a file path corresponding to Uri
 * @return
 */
private fun getDataColumn(
    context: Context, uri: Uri,
    selection: String?,
    selectionArgs: Array<String>?
): String {
    var path: String = ""

    val projection = arrayOf(MediaStore.Images.Media.DATA)
    var cursor: Cursor? = null
    try {
        cursor =
            context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(projection[0])
            path = cursor.getString(columnIndex)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        cursor?.close()
    }
    return path
}

/**
 * @param uri the Uri to check
 * @return Whether the Uri authority is MediaProvider
 */
private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

/**
 * @param uri the Uri to check
 * @return Whether the Uri authority is DownloadsProvider
 */
private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}
