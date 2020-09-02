package com.activedev.todo_note

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import java.util.*

object NotificationScheduler {

    fun showNotification(
        context: Context,
        cls: Class<*>?,
        text: String?,
        requestCode: Int,
        priority: String?
    ) {
        val notifySound =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationIntent = Intent(context, cls)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val stackBuilder =
            TaskStackBuilder.create(context)
        stackBuilder.addParentStack(cls)

        stackBuilder.addNextIntent(notificationIntent)
//        val pendingIntent = stackBuilder.getPendingIntent(
//            requestCode,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph1)
            .setDestination(R.id.todoFragment)
            .createPendingIntent()

        val builder =
            NotificationCompat.Builder(context, "my_channel_01")

        builder.setContentTitle("Reminder")
            .setContentText("It's time to do $text")
            .setAutoCancel(true)
            .setWhen(Calendar.getInstance().timeInMillis)
            .setSound(notifySound)
            .setVibrate(longArrayOf(1000, 1000, 1000))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setLights(Color.RED, 3000, 3000)
            .setContentIntent(pendingIntent)

        var importance = 0
        when (priority) {
            "High" -> importance = 4
            "Medium" -> importance = 1
            "Low" -> importance = 2
            "None" -> importance = 3
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    "my_channel_01",
                    "Reminder",
                    importance
                )

            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.enableVibration(true)

            notificationManager.createNotificationChannel(channel)
        } else {
            when (priority) {
                "High" -> builder.priority = Notification.PRIORITY_HIGH
                "Medium" -> builder.priority = Notification.PRIORITY_MIN
                "Low" -> builder.priority = Notification.PRIORITY_LOW
                "None" -> builder.priority = Notification.PRIORITY_DEFAULT
            }
        }

        val notification = builder.build()
        notificationManager.notify(requestCode, notification)
    }
}