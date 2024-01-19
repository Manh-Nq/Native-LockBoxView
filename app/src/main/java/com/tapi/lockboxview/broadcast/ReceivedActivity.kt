package com.tapi.lockboxview.broadcast

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tapi.lockboxview.databinding.ReceiveActivityBinding


class ReceivedActivity : AppCompatActivity() {
    private lateinit var binding: ReceiveActivityBinding

    private lateinit var myBroadcast: MyBroadcast
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ReceiveActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        myBroadcast = MyBroadcast()
        registerReceiver(myBroadcast, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(myBroadcast)
    }
}