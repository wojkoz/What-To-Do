package com.example.whattodo.utils.files

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

fun readTextFromUri(
    uri: Uri,
    context: Context,
): String? {
    return try {
        val stringBuilder = StringBuilder()
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = reader.readLine()
                }
            }
        }
        stringBuilder.toString()
    } catch (e: Exception) {
        Log.e("readTextFromUri", e.toString())
        null
    }
}