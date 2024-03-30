package com.gradle.utilities.notifications

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.remindmed.R
import com.gradle.constants.CHANNEL_ID
import java.util.Date
import com.gradle.utilities.notifications.NotificationUtils.Companion.createPendingIntent

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println("Sending notification")

        var notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.bell)
            .setContentTitle(intent.getStringExtra("title"))
            .setContentText(intent.getStringExtra("content"))
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify((Date().time / 1000L % Int.MAX_VALUE).toInt(), notification)

        if (intent.getLongExtra("end", 0) < System.currentTimeMillis()) {
            val pendingIntent = PendingIntent.getService(context, intent.getIntExtra("requestCode", 0), intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)
            if (pendingIntent != null) {
                NotificationUtils.cancelNotification(context, pendingIntent)
            }
        } else {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = createPendingIntent(
                context, intent.getStringExtra("title"),
                intent.getStringExtra("content"),
                intent.getLongExtra("end", 0)
            )
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 86400000, pendingIntent)
        }
    }
}