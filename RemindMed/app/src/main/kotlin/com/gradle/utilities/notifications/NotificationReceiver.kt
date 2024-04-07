package com.gradle.utilities.notifications

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.cs346.remindmed.R
import com.gradle.apiCalls.PatientApi
import com.gradle.constants.CHANNEL_ID
import com.gradle.utilities.notifications.NotificationUtils.Companion.createPendingIntent
import java.util.Date
import kotlin.math.abs

class NotificationReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        println("Received notification broadcast")

        var pid = intent.getStringExtra("pid")
        var mid = intent.getStringExtra("mid")

        if (pid == null || mid == null) {
            return
        } else {
            val medications = PatientApi().getMedicines(pid)
            var sendNotification = false

            for (med in medications) {
                if (med.medicationId == intent.getStringExtra("mid")) {
                    val currentTime = Date(System.currentTimeMillis())
                    sendNotification = true

                    var foundTime = false
                    for (time in med.times) {
                        val timeDate = Date(time.time)
                        if (abs(timeDate.minutes - currentTime.minutes) <= 5) {
                            foundTime = true
                            break
                        }
                    }
                    if (!foundTime) {
                        sendNotification = false
                    }

                    break
                }
            }

            if (!sendNotification) {
                return
            }
        }

        var notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.bell)
            .setContentTitle(intent.getStringExtra("title"))
            .setContentText(intent.getStringExtra("content"))
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (intent.getLongExtra("end", 0) < System.currentTimeMillis()) {
            val pendingIntent = PendingIntent.getService(
                context, intent.getIntExtra("requestCode", 0), intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                NotificationUtils.cancelNotification(context, pendingIntent)
            }
        } else {
            manager.notify((Date().time / 1000L % Int.MAX_VALUE).toInt(), notification)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = createPendingIntent(
                context, intent.getStringExtra("title"),
                intent.getStringExtra("content"),
                intent.getLongExtra("end", 0),
                intent.getStringExtra("pid"),
                intent.getStringExtra("mid"),
            )
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 86400000,
                pendingIntent
            )
        }
    }
}
