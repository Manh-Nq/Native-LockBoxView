package com.tapi.lockboxview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.tapi.lockboxview.notify.NotificationHelper


class MainActivity : AppCompatActivity() {

    var btView: AppCompatTextView? = null
    var requestBtn: AppCompatButton? = null
    var pushBtn: AppCompatButton? = null
    var permissionBtn: AppCompatButton? = null

    private val pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        Log.d("ManhNQ", "granted : $granted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.text_layout_fragment)
        btView = findViewById(R.id.battery_view)
        requestBtn = findViewById(R.id.request_btn)
        pushBtn = findViewById(R.id.push_noti_btn)
        permissionBtn = findViewById(R.id.request_per)

        btView?.text = toScheduleContent2()

        requestBtn?.setOnClickListener {
            enableTouchVibrate()
        }
        pushBtn?.setOnClickListener {

            NotificationHelper.createBatteryModeNotification(this, "channel 1", 1)
        }
        permissionBtn?.setOnClickListener {
            NotificationHelper.createBatteryModeNotification2(this, "channel 2", 2)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pushNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val result = getRowOf(5, 2)
        Log.d("ManhNQ", "onCreate: $result")
    }


    fun enableTouchVibrate() {
        if (!canWriteSetting()) {
            requestWriteSettingPermission()
        } else {
            Settings.System.putInt(contentResolver, Settings.System.HAPTIC_FEEDBACK_ENABLED, 1)
        }
    }

    private fun AppCompatActivity.canWriteSetting(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Settings.System.canWrite(this) else true
    }

    private fun AppCompatActivity.requestWriteSettingPermission() {
        val intent = Intent("android.settings.action.MANAGE_WRITE_SETTINGS")
        intent.data = Uri.parse("package:$packageName")
        startActivityForResult(intent, 1009)
    }


    private fun Context.toScheduleContent2(): SpannableStringBuilder {
        val content = String.format(
            getString(R.string.switch_to_mode_from_time_to_time_format),
            "Mode Testhjkfjksdhfks",
            "7h20",
            "19h40dasdlasjkldjaksl"
        )

        val start = content.indexOf("Mode Testhjkfjksdhfks")
        val end = start + "Mode Testhjkfjksdhfks".length

        val start2 = content.indexOf("7h20")
        val end2 = start2 + "7h20".length

        val start3 = content.lastIndexOf("19h40dasdlasjkldjaksl")
        val end3 = start3 + "19h40dasdlasjkldjaksl".length

        return SpannableStringBuilder(content).apply {
            setSpanColor(
                this@toScheduleContent2,
                R.color.purple_700,
                start,
                end
            )
            setSpanColor(
                this@toScheduleContent2,
                R.color.purple_700,
                start2,
                end2
            )
            setSpanColor(
                this@toScheduleContent2,
                R.color.purple_700,
                start3,
                end3
            )
        }
    }

    private fun Context.toScheduleContent(): SpannableStringBuilder {
        val content = String.format(
            getString(R.string.switch_to_mode_from_time_to_time_format),
            "<Mode Testhjkfjksdhfks>",
            "<7h20>",
            "<19h40dasdlasjkldjaksl>"
        )

        val result = getTimePairs(content)
        val times = result.first.absolute()
        val contentStr = result.second


        return SpannableStringBuilder(contentStr).apply {
            setSpanColor(
                this@toScheduleContent,
                R.color.purple_700,
                times[0].first,
                times[0].second
            )
            setSpanColor(
                this@toScheduleContent,
                R.color.purple_700,
                times[1].first,
                times[1].second
            )
            setSpanColor(
                this@toScheduleContent,
                R.color.purple_700,
                times[2].first,
                times[2].second
            )
        }

    }

    private fun getTimePairs(input: String): Pair<MutableList<Pair<Int, Int>>, String> {
        val pattern = "<(.*?)>".toRegex()
        val matches = pattern.findAll(input).toList()

        val pairIndices = mutableListOf<Pair<Int, Int>>()
        for (index in matches.indices) {

            val match = matches[index]

            val startIndex = match.range.first
            val endIndex = match.range.last
            pairIndices.add(startIndex to endIndex)
        }
        return pairIndices to input.replaceTags()
    }

    private fun MutableList<Pair<Int, Int>>.absolute(): List<Pair<Int, Int>> {
        return mapIndexed { index, pair ->
            val start = pair.first - index * 2
            val end = start + (pair.second - pair.first) - 1
            start to end
        }
    }

    private fun String.replaceTags(): String {
        return replace("<", "").replace(">", "")
    }

    fun SpannableStringBuilder.setSpanColor(
        context: Context, colorId: Int, start: Int, end: Int
    ) {
        setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    context, colorId
                )
            ), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun getRowOf(totalCount: Int, spanCount: Int): List<IndexType> {
        val totalRow = (totalCount + spanCount - 1) / spanCount
        val maxCount = totalRow * spanCount

        val result = mutableListOf<IndexType>()

        for (i in 0 until totalCount) {
            val index = i + 1
            val currentRow = currentRow(spanCount, index)
            Log.d("ManhNQ", "getRowOf: $currentRow  - index $index")
            if (currentRow == 1) {
                if (totalRow == currentRow) {
                    if (index % spanCount == 1) {
                        result.add(IndexType.BOTTOM_LEFT)
                    } else if (index % spanCount == 0) {
                        result.add(IndexType.BOTTOM_RIGHT)
                    } else {
                        result.add(IndexType.NORMAL)
                    }
                } else {
                    if (index % spanCount == 1) {
                        result.add(IndexType.TOP_LEFT)
                    } else if (index % spanCount == 0) {
                        result.add(IndexType.TOP_RIGHT)
                    } else {
                        result.add(IndexType.NORMAL)
                    }
                }
            } else if (currentRow > 1 && currentRow == totalRow) {
                if (index % spanCount == 1) {
                    result.add(IndexType.BOTTOM_LEFT)
                } else if (index % spanCount == 0) {
                    result.add(IndexType.BOTTOM_RIGHT)
                } else {
                    result.add(IndexType.NORMAL)
                }
            } else {
                result.add(IndexType.NORMAL)
            }
        }

        val remain = maxCount - totalCount
        if (remain > 0) {
            for (i in 0 until remain) {
                result.add(IndexType.NONE)
            }
        }
        return result

    }
    fun currentRow(spanCount: Int, currentIndex: Int): Int {
        return (currentIndex + spanCount - 1) / spanCount
    }


}

enum class IndexType {
    TOP_LEFT, TOP_RIGHT, NORMAL, BOTTOM_LEFT, BOTTOM_RIGHT, NONE
}