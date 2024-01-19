package com.tapi.lockboxview.service

import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class TestService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val imageUrl = intent.getStringExtra("KEY_URL")
            try {
                if (imageUrl != null) {
                   DownloadService.downloadImage(imageUrl,object :OnDownloadCallBack{
                       override fun onProgress(percent: Int) {
                           Log.d("ManHNQ", "onProgress: ")
                       }

                       override fun onComplete() {
                           Log.d("ManHNQ", "onComplete: ")
                       }

                       override fun onFailed() {
                           Log.d("ManHNQ", "onFailed: ")
                       }
                   })
                }
            } catch (e: IOException) {
                Log.e("ImageDownloadService", "Error downloading image", e)
            }
        }
        return START_STICKY
    }




    override fun onCreate() {
        super.onCreate()
        Log.d("ManhNQ", "onCreate: ")
    }


}