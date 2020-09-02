package com.activedev.todo_note

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

object AlarmScheduler {

    fun setReminder(
        context: Context,
        cls: Class<*>,
        todoText: String,
        time: Long,
        reminderId: Int,
        reminderType: String,
        priority: String
    ) {

        cancelReminder(context, cls, reminderId)

        val receiver = ComponentName(context, cls)
        val packageManager = context.packageManager
        packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        val alarmIntent = Intent(context, cls)
        alarmIntent.putExtra("text", todoText)
        alarmIntent.putExtra("reminderType", reminderType)
        alarmIntent.putExtra("remindId", reminderId)
        alarmIntent.putExtra("priority", priority)
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
                    time,
                    pendingIntent
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 -> {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            }
        }
    }

    private fun cancelReminder(context: Context, cls: Class<*>, reminderId: Int) {

        val receiver = ComponentName(context, cls)
        val packageManager = context.packageManager
        packageManager.setComponentEnabledSetting(
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