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
import java.sql.Date
import java.sql.Time
import java.time.LocalDate
import kotlin.time.Duration.Companion.days

//import java.util.Calendar.kt

class NotificationUtils {

    companion object {
        private var requestCode = 0

        @RequiresApi(Build.VERSION_CODES.S)
        fun scheduleNotifications(context: Context, patient: Patient, medication: Medication) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (notificationServicePermission(context)) {
                if (alarmManager.canScheduleExactAlarms() == false) {
                    Intent().also { intent ->
                        intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                        context.startActivity(intent)
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
                    for (time in medication.times) {
                        val alarm = Calendar.getInstance()
                        alarm.set(Calendar.HOUR_OF_DAY, time.hours)
                        alarm.set(Calendar.MINUTE, time.minutes)
                        alarm.set(Calendar.SECOND, time.seconds)
                        alarm.set(Calendar.DAY_OF_YEAR, dayOfYear(medication.startDate.time))

                        val pendingIntent = createPendingIntent(
                            context,
                            "Reminder to take your " + medication.name,
                            "Hey ${patient.name}, remember to take ${medication.amount} of your ${medication.name}.",
                            medication.endDate.time,
                            patient.pid,
                            medication.medicationId
                        )

                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.timeInMillis, pendingIntent)
                    }
                }
            }
        }

        fun createPendingIntent(
            context: Context,
            title: String?,
            content: String?,
            endTime: Long?,
            pid: String?,
            mid: String?
        ): PendingIntent {
            if (title == null || content == null || endTime == null) {
                return PendingIntent.getBroadcast(context, 0, Intent(), PendingIntent.FLAG_IMMUTABLE)
            }

            val intent = Intent(context, NotificationReceiver::class.java)
            intent.putExtra("title", title)
            intent.putExtra("content", content)
            intent.putExtra("end", endTime)
            intent.putExtra("requestCode", requestCode)
            intent.putExtra("pid", pid)
            intent.putExtra("mid", mid)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            requestCode += 1

            return pendingIntent
        }

        fun cancelNotification(context: Context, intent: PendingIntent) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(intent)
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

        fun dayOfYear(epochMilliseconds: Long): Int {
            // Convert epoch milliseconds to java.sql.Time
            val time = Time(epochMilliseconds)

            // Convert java.sql.Time to java.util.Date
            val date = Date(time.time)

            // Convert java.util.Date to Calendar
            val calendar = Calendar.getInstance()
            calendar.time = date

            // Extract day of the year
            return calendar.get(Calendar.DAY_OF_YEAR)
        }
    }
}
