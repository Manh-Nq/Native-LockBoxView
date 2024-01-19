package com.tapi.lockboxview

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class AnimationSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_animation_activity)
        val btn= findViewById<AppCompatButton>(R.id.anim_btn)
        val anim = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim)
        btn.startAnimation(anim)
    }


}