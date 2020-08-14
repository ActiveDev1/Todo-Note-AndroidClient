package com.activedev.todo_note

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder

object NotificationScheduler {

    fun showNotification(
        context: Context,
        cls: Class<*>?,
        text: String?,
        requestCode: Int
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

        val notification = builder.setContentTitle("Reminder")
            .setContentText(text)
            .setAutoCancel(true)
            .setSound(notifySound)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent).build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    "my_channel_01",
                    "Reminder",
                    NotificationManager.IMPORTANCE_HIGH
                )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(requestCode, notification)
        } else {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(requestCode, notification)

        }

    }
}