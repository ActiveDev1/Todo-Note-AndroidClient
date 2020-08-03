package com.activedev.todo_note

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.activedev.todo_note.adapters.TodoAdapter
import com.activedev.todo_note.databinding.FragmentTodoBinding
import com.activedev.todo_note.model.Todo
import com.activedev.todo_note.network.Api
import com.shashank.sony.fancytoastlib.FancyToast
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class TodoFragment : Fragment() {

    lateinit var sharedPref: SharedPreferences
    private lateinit var todoAdapter: TodoAdapter
    private var todo: MutableList<Todo> = ArrayList<Todo>()
    private lateinit var binding: FragmentTodoBinding
    private val todoList: List<Todo>? = todo


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = ""

        binding.floatingActionButton.setOnClickListener {
            Toast.makeText(context, "Testing ...", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = activity?.getSharedPreferences(
            R.string.preference_file_key.toString(),
            Context.MODE_PRIVATE
        ) ?: return

        val token = sharedPref.getString(R.string.token.toString(), "")

        todoAdapter = TodoAdapter(todo)
        binding.todoList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.todoList.adapter = todoAdapter

        todoAdapter.setOnItemClickListener(object : TodoAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onBtnClick(position: Int) {
                val todo = todoList?.get(position)
//                if (todo?.isFavored.equals("0"))

            }

        })

        try {
            val call: Call<ResponseBody> = Api.retrofitService.getTodo(token)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()!!.string()
                        if (response.isSuccessful) {
                            try {
                                if (res.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        "No Todo",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val resp = JSONObject(res)
                                    val dataArray = resp.getJSONArray("data")
                                    for (i in 0 until dataArray.length()) {
                                        val jsonObject: JSONObject = dataArray.getJSONObject(i)

                                        val id = jsonObject.getString("id")
                                        val text = jsonObject.getString("text")
                                        val dueDate = jsonObject.getString("due_date")
                                        val createdAt = jsonObject.getString("created_at")
                                        val updatedAt = jsonObject.getString("updated_at")
                                        val isDone = jsonObject.getString("is_done")
                                        val isFavored = jsonObject.getString("is_favored")

                                        todo.add(
                                            Todo(
                                                id,
                                                text,
                                                dueDate,
                                                createdAt,
                                                updatedAt,
                                                isDone,
                                                isFavored
                                            )
                                        )

                                    }
                                    todoAdapter.notifyDataSetChanged()

                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                        }


                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    FancyToast.makeText(
                        context,
                        "Error on connection !!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR, false
                    ).show()
                }
            })
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}