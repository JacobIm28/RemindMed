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
import com.example.remindmed.R
import com.gradle.constants.CHANNEL_ID
import com.gradle.models.Medication
import java.util.Date
import com.gradle.utilities.notifications.NotificationUtils.Companion.createPendingIntent
import com.gradle.apiCalls.PatientApi as PatientApi

class NotificationReceiver: BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        println("Sending notification")

        var pid = intent.getStringExtra("pid")
        var mid = intent.getStringExtra("mid")
        Medication


        if (pid == null || mid == null) {
            return
        } else {
            var result = PatientApi().getMedicines(pid)
            var foundMedication = false

            for (med in result) {
                if (med.medicationId == intent.getStringExtra("mid")) {
                    foundMedication = true
                    break
                }
            }

            if (!foundMedication) {
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
            val pendingIntent = PendingIntent.getService(context, intent.getIntExtra("requestCode", 0), intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)
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
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 86400000, pendingIntent)
        }
    }
}