package com.tapi.lockboxview.service

import android.os.Environment
import android.os.Handler
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

interface OnDownloadCallBack {
    fun onProgress(percent: Int)
    fun onComplete()
    fun onFailed()
}

class DownloadTask(
    private val imageUrl: String,
    private val callBack: OnDownloadCallBack,
    private val uiHandler: Handler
) :
    Runnable {
    override fun run() {
        downloadImage(imageUrl = imageUrl)
    }

    private fun downloadImage(imageUrl: String) {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val contentLength = connection.contentLength
            val input = connection.inputStream

            val buffer = ByteArray(1024)
            var bytesRead: Int
            var totalBytesRead = 0
            val fileOutputStream = FileOutputStream(getOutputFile())

            while (input.read(buffer).also { bytesRead = it } != -1) {
                totalBytesRead += bytesRead

                fileOutputStream.write(buffer, 0, bytesRead)

                val progress = (totalBytesRead * 100L / contentLength).toInt()
                uiHandler.post { callBack.onProgress(percent = progress) }
                Log.d("ManhNQ", "downloadImage: $progress")
            }

            input.close()
            connection.disconnect()
            uiHandler.post { callBack.onComplete() }
        } catch (e: IOException) {
            Log.e("ManhNQ", "Error downloading image", e)
            uiHandler.post { callBack.onFailed() }
        }
    }

    private fun getOutputFile(): File {
        val externalDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "video.mp4"
        return File(externalDir, fileName)
    }
}