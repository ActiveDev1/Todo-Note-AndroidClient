package com.activedev.todo_note.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.activedev.todo_note.R
import com.activedev.todo_note.adapters.TodoAdapter.TodoViewHolder
import com.activedev.todo_note.database.Reminder
import com.activedev.todo_note.database.ReminderDatabase
import com.activedev.todo_note.network.TodoRequest
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TodoAdapter(val context: Context, private val todoList: List<Reminder>, token: String?) :
    RecyclerView.Adapter<TodoViewHolder>() {
    private val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())


    private var request = TodoRequest(context, token)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.todo_details, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {

        val item = todoList[position]
        holder.btnFavored.setOnClickListener {

            val reminderDao =
                context.let { it1 -> ReminderDatabase.getInstance(it1).reminderDatabaseDao }

            if (item.isFavored == "0") {
                request.updateTodo(
                    item.remindId,
                    item.text,
                    formatter.format(item.dueDate),
                    item.isDone,
                    "1"
                )
                reminderDao.updateFavored(item.remindId, "1")
//                todoList[position].isFavored = "1"
                holder.btnFavored.setBackgroundResource(R.drawable.ic_star_on)
                Log.i("sssss", "To 1")

            } else if (item.isFavored == "1") {
                request.updateTodo(
                    item.remindId,
                    item.text,
                    formatter.format(item.dueDate),
                    item.isDone,
                    "0"
                )
                reminderDao.updateFavored(item.remindId, "0")
//                todoList[position].isFavored = "0"
                holder.btnFavored.setBackgroundResource(R.drawable.ic_star_off)
                Log.i("sssss", "To 0")

            }
        }
        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    class TodoViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        var btnFavored: ImageButton = itemView.findViewById(R.id.btnFavored)
        private var text: TextView = itemView.findViewById(R.id.textTodo)
        var dueDate: TextView = itemView.findViewById(R.id.dueData)
        private val formatter: DateFormat =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())


        fun bind(item: Reminder) {
            text.text = item.text
            dueDate.text = formatter.format(item.dueDate)
            if (item.isFavored == "0")
                btnFavored.setBackgroundResource(R.drawable.ic_star_off)
            else btnFavored.setBackgroundResource(R.drawable.ic_star_on)
        }
    }
}