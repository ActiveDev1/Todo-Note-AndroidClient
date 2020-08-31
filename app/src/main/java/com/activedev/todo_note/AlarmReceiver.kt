package com.activedev.todo_note

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show()
        val textTodo = intent.getStringExtra("text")
        val requestCode = intent.getIntExtra("remindId", 1)
        NotificationScheduler.showNotification(
            context,
            MainActivity::class.java,
            textTodo,
            requestCode
        )

    }
}