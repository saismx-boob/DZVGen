package com.example.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.example.data.local.CreationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object MediaDownloadManager {

    suspend fun downloadMediaToLocal(
        context: Context,
        creation: CreationEntity
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val isVideo = creation.type == "VIDEO"
            val timestamp = System.currentTimeMillis()
            val fileName = "VisionAI_${if (isVideo) "video" else "art"}_$timestamp"
            val mimeType = if (isVideo) "video/mp4" else "image/png"
            val subFolder = if (isVideo) "Movies/VisionAI Studio" else "Pictures/VisionAI Studio"

            val sourceStream: InputStream? = getInputStreamForUrl(context, creation.mediaUrl)

            if (sourceStream == null) {
                return@withContext Result.failure(Exception("Fichier source introuvable."))
            }

            val savedUri: Uri?
            val resolver = context.contentResolver

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.${if (isVideo) "mp4" else "png"}")
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, subFolder)
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }

                val collectionUri = if (isVideo) {
                    MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                } else {
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                }

                savedUri = resolver.insert(collectionUri, contentValues)

                if (savedUri != null) {
                    resolver.openOutputStream(savedUri)?.use { out ->
                        sourceStream.copyTo(out)
                    }

                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(savedUri, contentValues, null, null)
                }
            } else {
                // Fallback for older Android versions
                val targetDir = Environment.getExternalStoragePublicDirectory(
                    if (isVideo) Environment.DIRECTORY_MOVIES else Environment.DIRECTORY_PICTURES
                )
                val appDir = File(targetDir, "VisionAI Studio")
                if (!appDir.exists()) appDir.mkdirs()

                val targetFile = File(appDir, "$fileName.${if (isVideo) "mp4" else "png"}")
                FileOutputStream(targetFile).use { out ->
                    sourceStream.copyTo(out)
                }
                savedUri = Uri.fromFile(targetFile)
            }

            if (savedUri != null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Téléchargé avec succès dans $subFolder !",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Result.success("Enregistré dans $subFolder")
            } else {
                Result.failure(Exception("Erreur lors de l'enregistrement MediaStore."))
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Erreur de téléchargement : ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            Result.failure(e)
        }
    }

    private fun getInputStreamForUrl(context: Context, url: String): InputStream? {
        return try {
            when {
                url.startsWith("file://") -> {
                    val file = File(Uri.parse(url).path ?: "")
                    if (file.exists()) FileInputStream(file) else null
                }
                url.startsWith("android.resource://") -> {
                    context.contentResolver.openInputStream(Uri.parse(url))
                }
                else -> {
                    // Try direct file path
                    val f = File(url)
                    if (f.exists()) FileInputStream(f) else null
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}
