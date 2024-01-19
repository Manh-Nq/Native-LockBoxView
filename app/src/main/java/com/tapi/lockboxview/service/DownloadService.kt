package com.tapi.lockboxview.service

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object DownloadService {

    private var executorService: ExecutorService = Executors.newCachedThreadPool()
    private var uiHandler: Handler = Handler(Looper.getMainLooper())


    fun downloadImage(imageUrl: String, onDownloadCallBack: OnDownloadCallBack) {
        executorService.execute(
            DownloadTask(
                imageUrl = imageUrl,
                callBack = onDownloadCallBack,
                uiHandler = uiHandler
            )
        )
    }

}