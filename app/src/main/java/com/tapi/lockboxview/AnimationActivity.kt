package com.tapi.lockboxview

import android.animation.ArgbEvaluator
import android.animation.TimeAnimator
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

enum class DeviceState(val value: String) {
    Safety("safe"), Problem("problem"), Dangerous("danger")
}

class AnimationActivity : AppCompatActivity() {
    private lateinit var bgView: View;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.animation_activity)
        bgView = findViewById(R.id.bg_view)

        bgView.animateBackground(DeviceState.Safety, DeviceState.Dangerous)

    }

    private fun View.animateBackground(fromState: DeviceState, toState: DeviceState) {
        val colors = mapOf(
            DeviceState.Safety.value to Pair(
                Color.parseColor("#7BE495"),
                Color.parseColor("#329D9C")
            ),
            DeviceState.Problem.value to Pair(
                Color.parseColor("#EBAD62"),
                Color.parseColor("#E9765B")
            ),
            DeviceState.Dangerous.value to Pair(
                Color.parseColor("#FB4861"),
                Color.parseColor("#B1329C")
            )
        )

        val (fromStartColor, fromEndColor) = colors[fromState.value] ?: return
        val (toStartColor, toEndColor) = colors[toState.value] ?: return

        startAnimation(fromStartColor, fromEndColor, toStartColor, toEndColor)
    }


    private fun View.startAnimation(
        fromStartColor: Int, fromEndColor: Int, toStartColor: Int, toEndColor: Int
    ) {

        val evaluator = ArgbEvaluator()
        val gradient = background as GradientDrawable

        val animator = TimeAnimator.ofFloat(0.0f, 1.0f)
        animator.duration = 1000

        animator.addUpdateListener { valueAnimator ->
            val fraction = valueAnimator.animatedFraction
            val newStart = evaluator.evaluate(fraction, fromStartColor, toStartColor) as Int
            val newEnd = evaluator.evaluate(fraction, fromEndColor, toEndColor) as Int
            val newArray = intArrayOf(newStart, newEnd)

            gradient.colors = newArray
        }
        animator.start()
    }
}
