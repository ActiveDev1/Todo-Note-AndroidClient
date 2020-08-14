package com.activedev.todo_note

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action != null) {
            if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED, ignoreCase = true)) {
                val textTodo = intent.getStringExtra("text")
                val requestCode = intent.getIntExtra("remindId", 1)
                NotificationScheduler.showNotification(
                    context,
                    MainActivity::class.java,
                    textTodo,
                    requestCode
                )
                return
            }
        }


    }
}