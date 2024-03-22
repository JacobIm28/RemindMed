package com.gradle.utilities.notifications

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.gradle.models.Medication
import com.gradle.models.Patient
import com.gradle.utilities.toFormattedDateString
import io.ktor.util.date.getTimeMillis
import java.time.LocalDate

//import java.util.Calendar

class NotificationUtils {

    companion object {
        @RequiresApi(Build.VERSION_CODES.S)
        fun scheduleNotifications(context: Context, patient: Patient, medication: Medication) {
            println("In scheduleNotifications")
            print(notificationServicePermission(context))
//            if (notificationServicePermission(context)) {
                val intent = Intent(context, NotificationReceiver::class.java)

                intent.putExtra("title", "Reminder to take your " + medication.name
                )
                intent.putExtra("content", "Hey ${patient.name}, rememeber to take ${medication.amount} of your ${medication.name}.")
                intent.putExtra("end", medication.endDate.time)

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    1,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                print(alarmManager.canScheduleExactAlarms())

                if (alarmManager.canScheduleExactAlarms() == false) {
                    Intent().also { intent ->
                        intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                        context.startActivity(intent)
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
                    // This is only here for testing purpose, the loop below will run normally
                    print("Scheduling notifications for 5 sec in the future")
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + 5000,
                        pendingIntent
                    )

//                    for (time in medication.times) {
//                        val alarm = Calendar.getInstance()
//                        alarm.set(Calendar.HOUR_OF_DAY, time.hours)
//                        alarm.set(Calendar.MINUTE, time.minutes)
//                        alarm.set(Calendar.SECOND, time.seconds)
//                        alarm.set(Calendar.DAY_OF_YEAR, LocalDate.now().dayOfYear + 1)
//
//                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
//                    }
                }
        }

        fun cancelNotification(context: Context, intent: Intent) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = PendingIntent.getService(context, 100, intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pendingIntent)
        }

        private fun notificationServicePermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (!notificationManager.areNotificationsEnabled()) {
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    context.startActivity(intent)
                    return false
                }
            } else {
                val areEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()

                if (!areEnabled) {
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    context.startActivity(intent)
                    return false
                }
            }
            return true
        }
    }
}