package com.tapi.lockboxview.notify

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tapi.lockboxview.MainActivity
import com.tapi.lockboxview.R


object NotificationHelper {

    val NOTIFICATION_BATTERY_MODE_CHANNEL_ID = "NOTIFICATION_BATTERY_MODE_CHANNEL_ID"
    val NOTIFICATION_BATTERY_MODE_NOTIFY_ID = 22

    fun createBatteryModeNotification(
        context: Context, channelId: String, notiId: Int
    ) {
        val notificationLayout = createBatteryNotificationLayout(
            context,
            "Title notifications",
            "Subtitle String"
        )

        val notificationExpanded = createBatteryNotificationExpanded(
            context,
            "Title notifications",
            "Subtitle String"
        )

        val intentToMain = Intent(context, MainActivity::class.java)
        intentToMain.addCategory(Intent.CATEGORY_LAUNCHER)
        intentToMain.action = Intent.ACTION_MAIN
        intentToMain.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP


        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.power_saver_smart_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationExpanded)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)

        setChannelIdWith(notificationManager, channelId)

        notificationManager.safePushNotify(
            context = context,
            id = notiId,
            notification = notification
        )
    }

    fun createBatteryModeNotification2(
        context: Context, channelId: String, notiId: Int
    ) {
        val notificationLayout = createBatteryNotificationLayout(
            context,
            "Title notifications",
            "Subtitle String"
        )

        val notificationExpanded = createBatteryNotificationExpanded(
            context,
            "Title notifications",
            "Subtitle String"
        )

        val intentToMain = Intent(context, MainActivity::class.java)
        intentToMain.addCategory(Intent.CATEGORY_LAUNCHER)
        intentToMain.action = Intent.ACTION_MAIN
        intentToMain.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intentToMain, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = Notification.Builder(context, channelId)
            .setSmallIcon(R.drawable.power_saver_smart_icon)
            .setPriority(Notification.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setContentIntent(pendingIntent)
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationExpanded)

        // For compatibility with Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Battery Mode Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notiId, notification)
    }

    private fun setChannelIdWith(
        notificationManager: NotificationManagerCompat,
        channelId: String
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        notificationManager.getNotificationChannel(channelId)?.let {
            notificationManager.createNotificationChannel(it)
            return
        }

        val channel =
            NotificationChannel(channelId, "New Channel-NAME", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }

    private fun createBatteryNotificationLayout(
        context: Context,
        title: String,
        subTitle: String
    ): RemoteViews {
        val notificationLayout =
            RemoteViews(context.packageName, R.layout.battery_notification_layout)
        notificationLayout.setTextViewText(R.id.notification_title, title)
        notificationLayout.setTextViewText(R.id.notification_subtitle, subTitle)

        return notificationLayout
    }

    private fun createBatteryNotificationExpanded(
        context: Context,
        title: String,
        subTitle: String
    ): RemoteViews {
        val notificationLayout =
            RemoteViews(context.packageName, R.layout.battery_notification_layout_expanded)
        notificationLayout.setTextViewText(R.id.notification_title, title)
        notificationLayout.setTextViewText(R.id.notification_subtitle, subTitle)

        return notificationLayout
    }

    private fun NotificationManagerCompat.safePushNotify(
        context: Context,
        id: Int,
        notification: Notification
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notify(id, notification)
    }

}

fun getPendingIntentFlag(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
}


fun Context.canWriteSetting(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Settings.System.canWrite(this) else true
}