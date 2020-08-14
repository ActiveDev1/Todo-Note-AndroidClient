package com.activedev.todo_note

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object AlarmSheduler {

    fun setReminder(
        context: Context?,
        cls: Class<*>?,
        todoText: String,
        dueDate: String,
        reminderId: Int
    ) {
        val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance(Locale.getDefault())
        val remind: Date = formatter.parse(dueDate)!!
        val setCalendar = Calendar.getInstance()
        setCalendar.time = remind

        // cancel already scheduled reminders
        cancelReminder(context!!, cls, reminderId)
        if (setCalendar.before(calendar)) setCalendar.add(Calendar.DATE, 1)

        val receiver = ComponentName(context, cls!!)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        val alarmIntent = Intent(context, cls)
        alarmIntent.putExtra("text", todoText)
//        alarmIntent.putExtra("dueDate", remind.toString())
        alarmIntent.putExtra("remindId", reminderId)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    setCalendar.timeInMillis,
                    pendingIntent
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 -> {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    setCalendar.timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    private fun cancelReminder(context: Context, cls: Class<*>?, reminderId: Int) {

        val receiver = ComponentName(context, cls!!)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        val intent1 = Intent(context, cls)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId,
            intent1,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }


}