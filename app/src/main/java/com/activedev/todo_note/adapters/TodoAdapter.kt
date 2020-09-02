package com.activedev.todo_note.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
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

            } else if (item.isFavored == "1") {
                request.updateTodo(
                    item.remindId,
                    item.text,
                    formatter.format(item.dueDate),
                    item.isDone,
                    "0"
                )
                reminderDao.updateFavored(item.remindId, "0")
                holder.btnFavored.setBackgroundResource(R.drawable.ic_star_off)

            }
        }
        val oldColors: ColorStateList = holder.dueDate.textColors


        holder.btnDone.setOnClickListener {

            val reminderDao =
                context.let { it1 -> ReminderDatabase.getInstance(it1).reminderDatabaseDao }

            if (item.isDone == "0") {
                request.updateTodo(
                    item.remindId,
                    item.text,
                    formatter.format(item.dueDate),
                    "1",
                    item.isFavored
                )
                reminderDao.updateDone(item.remindId, "1")
                holder.btnDone.setBackgroundResource(R.drawable.ic_is_done)
                holder.view.setBackgroundResource(R.color.viewDone)
                holder.todoText.apply {
                    setBackgroundResource(R.drawable.strike_through_text)
                    setTextColor(Color.GRAY)
                }
                holder.dueDate.apply {
                    text = formatter.format(item.dueDate)
                    setTextColor(Color.GRAY)
                }

            } else if (item.isDone == "1") {
                request.updateTodo(
                    item.remindId,
                    item.text,
                    formatter.format(item.dueDate),
                    "0",
                    item.isFavored
                )
                reminderDao.updateDone(item.remindId, "0")
                holder.btnDone.setBackgroundResource(R.drawable.ic_not_done)
                holder.view.setBackgroundResource(R.color.colorPrimary)
                holder.todoText.apply {
                    setBackgroundColor(Color.parseColor("#FFFFFF"))
                    setTextColor(Color.BLACK)
                }
                holder.dueDate.apply {
                    text = formatter.format(item.dueDate)
                    setTextColor(oldColors)
                }

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
        var todoText: TextView = itemView.findViewById(R.id.textTodo)
        var dueDate: TextView = itemView.findViewById(R.id.dueData)
        var view: View = itemView.findViewById(R.id.view)
        var btnFavored: ImageButton = itemView.findViewById(R.id.btnFavored)
        var btnDone: ImageButton = itemView.findViewById(R.id.imgDone)
        private val formatter: DateFormat =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())


        fun bind(item: Reminder) {
            if (item.isDone == "1") {
                todoText.apply {
                    setBackgroundResource(R.drawable.strike_through_text)
                    text = item.text
                    setTextColor(Color.GRAY)
                }
//                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//                binding.timeDone.visibility = View.VISIBLE
//                dueDate.text = "Completed on ${df.format(c)}"
                dueDate.apply {
                    text = formatter.format(item.dueDate)
                    setTextColor(Color.GRAY)
                }
                view.setBackgroundResource(R.color.viewDone)
                btnDone.setBackgroundResource(R.drawable.ic_is_done)
            } else {
                todoText.text = item.text
                dueDate.text = formatter.format(item.dueDate)
            }
            if (item.isFavored == "1")
                btnFavored.setBackgroundResource(R.drawable.ic_star_on)
        }
    }
}