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
import com.activedev.todo_note.model.Todo
import com.activedev.todo_note.network.Request

class TodoAdapter(private val todoList: MutableList<Todo>, token: String?) :
    RecyclerView.Adapter<TodoViewHolder>() {
    var context: Context? = null
    private var mListener: OnItemClickListener? = null

    //    var list: MutableList<Todo> = todoList
    var request = Request(context, token, todoList)


    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.todo_details, parent, false)
        return TodoViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {

        val item = todoList[position]
        holder.btnFavored.setOnClickListener {

            if (item.isFavored == "0") {
                request.updateTodo(item, item.id, item.text, item.dueDate, item.isDone, "1")
                todoList[position].isFavored = "1"
                holder.btnFavored.setImageResource(R.drawable.ic_star_on)
                Log.i("sssss", "To 1")

            } else if (item.isFavored == "1") {
                request.updateTodo(item, item.id, item.text, item.dueDate, item.isDone, "0")
                todoList[position].isFavored = "0"
                holder.btnFavored.setImageResource(R.drawable.ic_star_off)
                Log.i("sssss", "To 0")

            }
        }
        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    interface OnItemClickListener {
        fun onBtnClick(position: Int)
    }

    class TodoViewHolder(
        itemView: View,
        listener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(itemView) {
        var btnFavored: ImageButton = itemView.findViewById(R.id.btnFavored)
        private var text: TextView = itemView.findViewById(R.id.textTodo)
        var dueDate: TextView = itemView.findViewById(R.id.dueData)

        init {
            btnFavored.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION)
                        listener.onBtnClick(position)

                }
            }

        }

        fun bind(item: Todo) {
            text.text = item.text
            dueDate.text = item.dueDate
            if (item.isFavored == "0")
                btnFavored.setImageResource(R.drawable.ic_star_off)
            else btnFavored.setImageResource(R.drawable.ic_star_on)
        }
    }

}