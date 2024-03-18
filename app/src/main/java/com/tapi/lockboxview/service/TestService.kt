package com.tapi.lockboxview.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.tapi.lockboxview.IServiceListener
import java.io.IOException


class TestService : Service() {

    private var mBinder: IServiceListener.Stub = object : IServiceListener.Stub() {
        override fun sendData(data: String?) {
            Log.d("ManhNQ", "sendData: $data")
        }

    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val imageUrl = intent.getStringExtra("KEY_URL")
            try {
                if (imageUrl != null) {
                    DownloadService.downloadImage(imageUrl, object : OnDownloadCallBack {
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