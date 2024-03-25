package com.example.whattodo.utils.files

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

fun alterDocument(
    uri: Uri,
    context: Context,
    json: String,
    onError: (String) -> Unit,
    onSuccess: () -> Unit,
) {
    try {
        Log.e("alterDocument", " URI: ${uri.normalizeScheme()}")
        Log.e("alterDocument", " JSON: $json")
        context.contentResolver.openFileDescriptor(uri, "w")?.use {
            FileOutputStream(it.fileDescriptor).use {
                it.write(
                    json.toByteArray()
                )
            }
        }
    } catch (e: FileNotFoundException) {
        onError("File not found")
    } catch (e: IOException) {
        onError(e.message ?: "Error has occurred")
    }

    onSuccess()
}