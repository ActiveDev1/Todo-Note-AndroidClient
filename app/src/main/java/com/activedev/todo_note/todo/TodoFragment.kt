package com.activedev.todo_note.todo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.activedev.todo_note.R
import com.activedev.todo_note.adapters.TodoAdapter
import com.activedev.todo_note.database.Reminder
import com.activedev.todo_note.database.ReminderDatabase
import com.activedev.todo_note.database.ReminderDatabase.Companion.destroyInstance
import com.activedev.todo_note.database.ReminderDatabaseDao
import com.activedev.todo_note.databinding.FragmentTodoBinding
import com.activedev.todo_note.network.TodoRequest
import com.shashank.sony.fancytoastlib.FancyToast


class TodoFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var todoAdapter: TodoAdapter

    //private var todo: MutableList<Todo> = ArrayList<Todo>()
//    private var listTodo: List<Reminder> = ArrayList<Reminder>()

    private lateinit var binding: FragmentTodoBinding
    private lateinit var database: ReminderDatabase
    private lateinit var reminderDao: ReminderDatabaseDao

    lateinit var request: TodoRequest

    private var token: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = activity?.getSharedPreferences(
            R.string.preference_file_key.toString(),
            Context.MODE_PRIVATE
        )!!
        token = sharedPref.getString(R.string.token.toString(), "")

        database = ReminderDatabase.getInstance(context!!)
        reminderDao = database.reminderDatabaseDao

        request = TodoRequest(context!!, token)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = ""

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.todoList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
//        getTodo()
//        todoAdapter = TodoAdapter(context, listTodo, token)
//        binding.todoList.adapter = todoAdapter
//        todoAdapter.notifyDataSetChanged()
        request.allTodo()
        val todoList: List<Reminder> = reminderDao.getAllTodo()


        if (todoList.isEmpty()) {
            FancyToast.makeText(
                context,
                "No todo found!",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO, false
            ).show()
        }

        todoAdapter = TodoAdapter(context, todoList, token)
        binding.todoList.adapter = todoAdapter
        todoAdapter.notifyDataSetChanged()


        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_todoFragment_to_addTodoFragment)
        }

        binding.todoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0 && binding.floatingActionButton.visibility === View.VISIBLE) {
                    binding.floatingActionButton.hide()

                } else if (dy < 0 && binding.floatingActionButton.visibility !== View.VISIBLE) {
                    binding.floatingActionButton.show()
                }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        destroyInstance()
    }
}