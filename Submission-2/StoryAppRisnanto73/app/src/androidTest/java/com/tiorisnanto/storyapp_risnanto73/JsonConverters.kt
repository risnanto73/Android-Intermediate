package com.tiorisnanto.storyapp_risnanto73

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import java.io.IOException
import java.io.InputStreamReader

object JsonConverters {
    fun readStringFromFile(fileName: String): String {
        try {
            val applicationContext = ApplicationProvider.getApplicationContext<Context>()
            val inputStream = applicationContext.assets.open(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}