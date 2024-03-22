package com.gradle.utilities.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.remindmed.R
import com.gradle.constants.CHANNEL_ID
import com.gradle.constants.notificationId
import java.time.LocalDate
import java.util.Date

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println("Sending Notification")

//        if (intent.getLongExtra("end", System.currentTimeMillis() - 10000) < System.currentTimeMillis()) {
//            NotificationUtils.cancelNotification(context, intent)
//        } else {
            var notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("content"))
                .setAutoCancel(true)

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(notificationId, notification.build())
//        }


    }
}