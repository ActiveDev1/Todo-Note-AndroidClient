package com.activedev.todo_note

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import com.shashank.sony.fancytoastlib.FancyToast


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val textTodo = intent.getStringExtra("text")
        val reminderType = intent.getStringExtra("reminderType")
        val requestCode = intent.getIntExtra("remindId", 1)
        val priority = intent.getStringExtra("priority")
        when {
            reminderType.equals("Notification") -> NotificationScheduler.showNotification(
                context,
                MainActivity::class.java,
                textTodo,
                requestCode,
                priority
            )
            reminderType.equals("Alarm") -> {
/*
                  val pendingIntent = NavDeepLinkBuilder(context)
                      .setComponentName(MainActivity::class.java)
                      .setGraph(R.navigation.nav_graph1)
                      .setDestination(R.id.alarmFragment)
                      .createPendingIntent()

                val request = NavDeepLinkRequest.Builder
                    .fromUri("android-app://androidx.navigation.app/profile".toUri())
                    .build()
                findNavController().navigate(request)
                Navigation.findNavController(,R.id.nav_host_fragment1).navigate(pendingIntent)
                val fragment:AddTodoFragment
                findNavController(fragment).navigate(R.id.action_alarmFragment_to_todoFragment)*/
//                NavController(context).navigate(R.id.action_global_to_alarmFragment)

                val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                val ringtone = RingtoneManager.getRingtone(context, alarmUri)

                ringtone.play()
//                  val builder = AlertDialog.Builder(context.applicationContext)
//                  builder.setTitle("What you want to do ?")
//
//                  builder.setPositiveButton("Cancel") { _, _ ->
//                      ringtone.stop()
//                  }
//                  builder.setNegativeButton("Snooze") { _, _ ->
//                      ringtone.stop()
////                      suspend { snooze(context, ringtone) }
//                  }
//                  val dialog: AlertDialog = builder.create()
//                  dialog.show()
            }
            reminderType.equals("None") -> FancyToast.makeText(
                context,
                "It's time to do $textTodo ",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO,
                false
            ).show()
        }
    }

}