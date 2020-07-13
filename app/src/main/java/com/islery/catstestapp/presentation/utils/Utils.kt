package com.islery.catstestapp.presentation

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.islery.catstestapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun downloadImage(context: Context, url: String, scope: CoroutineScope) {
    Glide.with(context)
        .download(url)
        .listener(object : RequestListener<File> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<File>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: File?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<File>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.let {
                    scope.launch {
                        val success = downloadFile(resource, url, context)
                        if (success) {
                            val msg = context.getString(R.string.save_success)
                            showImageToast(msg, context)
                        } else {
                            val msg = context.getString(R.string.save_error)
                            showImageToast(msg, context)
                        }
                    }
                }
                return false
            }
        }
        )
        .submit()
}

suspend fun downloadFile(resource: File, url: String, context: Context): Boolean {

    val ext = MimeTypeMap.getFileExtensionFromUrl(url) ?: "jpg"
    val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) ?: "image/jpeg"
    val fileName = "cat${System.currentTimeMillis() / 1000}.$ext"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver = context.contentResolver!!
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val imageUri =
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let {
            return copyFileQandHigher(resource, resolver, imageUri)
        }
    } else {
        val myDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)!!
        val endFile = File(myDir, fileName)
        return copyFileBelowQ(resource, endFile)
    }
    return true
}


suspend fun copyFileBelowQ(oldFile: File, newFile: File) =
    withContext(Dispatchers.IO) {
        var inputStream: FileInputStream? = null
        var outputStream: FileOutputStream? = null
        try {

            inputStream = FileInputStream(oldFile)
            outputStream = FileOutputStream(newFile)
            val buffer = ByteArray(1024)
            while (inputStream.read(buffer) > 0) {
                outputStream.write(buffer)
            }
            return@withContext true
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        } finally {
            inputStream?.close()
            outputStream?.close()
        }

    }

suspend fun copyFileQandHigher(oldFile: File, resolver: ContentResolver, imageUri: Uri) =
    withContext(Dispatchers.IO) {
        val outputStream = resolver.openOutputStream(imageUri)
        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(oldFile)
            val buffer = ByteArray(1024)
            while (inputStream.read(buffer) > 0) {
                outputStream?.write(buffer)
            }
            return@withContext true
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }


fun showImageToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun Context.checkWriteAndReadPermissions(): Boolean {
    return (ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
            )
}

