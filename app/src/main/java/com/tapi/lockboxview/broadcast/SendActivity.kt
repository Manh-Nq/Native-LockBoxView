package com.tapi.lockboxview.broadcast

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.CycleInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.tapi.lockboxview.service.TestService
import com.tapi.lockboxview.databinding.SendActivityBinding


class SendActivity : AppCompatActivity() {
    private lateinit var binding: SendActivityBinding

    private lateinit var animation: ValueAnimator
    private val mDuration = 5000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SendActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendBtn.setOnClickListener {
            /* val intent = Intent()
             intent.setAction("test.sending")
             sendBroadcast(intent)

             startActivity(Intent(this, ReceivedActivity::class.java))*/
            val intent = Intent(this, TestService::class.java)
            intent.putExtra("KEY_URL", "https://tiengdong.com/wp-content/uploads/Video-dong-ho-dem-nguoc-1-phut-www_tiengdong_com.mp4?_=1")
            startService(intent)

        }
        binding.actionBtn.setOnClickListener {
            Log.d("ManhNQ", "onCreate: ${animation.isRunning}")

            if (animation.isPaused) {
                animation.resume()
            } else {
                animation.pause()
            }
        }


    }

    private fun initCurveAnim() {
        val path = Path()

        path.arcTo(0f, 0f, 1000f, 1000f, 270f, 180f, true)
        val animator: ObjectAnimator =
            ObjectAnimator.ofFloat(binding.viewAnimation3, View.X, View.Y, path)
        animator.setDuration(2000)
        animator.start()
    }

    private fun initValueAnim() {
        animation = ValueAnimator.ofFloat(0f, 1f)
        animation.apply {
            duration = mDuration
            interpolator = CycleInterpolator(1f)

            addUpdateListener { anim ->
                val value = anim.animatedValue as Float

                Log.d("ManhNQ", "value: $value")
                val width = convertValue(0f, 1f, 0f, 100f, value)

                val widthPx = dpToPx(this@SendActivity, width)

                Log.d("ManhNQ", "initAnim: $widthPx")

                val params = LinearLayout.LayoutParams(
                    widthPx.toInt(), widthPx.toInt()
                )

                binding.viewAnimation.layoutParams = params
            }
        }
        animation.start()

    }

    private fun initObjectAnim() {
        binding.viewAnimation2.pivotX = (binding.viewAnimation2.width / 2).toFloat()
        binding.viewAnimation2.pivotY = (binding.viewAnimation2.height / 2).toFloat()

        val animation = ObjectAnimator.ofFloat(binding.viewAnimation2, "rotation", 0f, 180f)
        animation.setDuration(mDuration)
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.start()
    }

    fun convertValue(min1: Float, max1: Float, min2: Float, max2: Float, value: Float): Float {
        return ((value - min1) * ((max2 - min2) / (max1 - min1)) + min2)
    }

    fun dpToPx(context: Context, dp: Float): Float {
        return (dp * context.resources.displayMetrics.density)
    }

    fun pxToDp(context: Context, px: Float): Float {
        return (px / context.resources.displayMetrics.density)
    }
}