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
import com.activedev.todo_note.network.Request
import java.util.*


class TodoFragment : Fragment() {

    lateinit var sharedPref: SharedPreferences
    private lateinit var todoAdapter: TodoAdapter
    private var todo: MutableList<Todo> = ArrayList<Todo>()
    private lateinit var binding: FragmentTodoBinding
    private val todoList: List<Todo> = todo


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = ""

//        sharedPref = activity?.getSharedPreferences(
//            R.string.preference_file_key.toString(),
//            Context.MODE_PRIVATE
//        )!!
//
//        val token = sharedPref.getString(R.string.token.toString(), "")
//
//        todoAdapter = TodoAdapter(todo)
//        binding.todoList.layoutManager = LinearLayoutManager(
//            context,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
//        binding.todoList.adapter = todoAdapter


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
        )!!

        val token = sharedPref.getString(R.string.token.toString(), "")

        todoAdapter = TodoAdapter(todo, token)
        val request = Request(context, token, todo)


        binding.todoList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.todoList.adapter = todoAdapter

        /* todoAdapter.setOnItemClickListener(object : TodoAdapter.OnItemClickListener {
             override fun onBtnClick(position: Int) {

                 val todo = todoList[position]


                 FancyToast.makeText(
                     context,
                    todo.isFavored + " test",
                     FancyToast.LENGTH_SHORT,
                     FancyToast.ERROR, false
                 ).show()


 //                if (todo.isFavored == "0") {
 //                    updateTodo(token, position, todo.id, todo.text, todo.dueDate, todo.isDone, "1")
 //                    Log.i("sssss", "To 1")
 //
 //                } else {
 //                    updateTodo(token, position, todo.id, todo.text, todo.dueDate, todo.isDone, "0")
 //                    Log.i("sssss", "To 0")
 //
 //                }
             }

         })*/
        request.allTodo(todoAdapter)
        todoAdapter.notifyDataSetChanged()
    }


    /*  private fun updateTodo(
          token: String?,
          position: Int,
          id: String,
          text: String,
          dueDate: String,
          isDone: String,
          isFavored: String
      ) {
          Log.i("sssss", "Nooooo")

          try {
              val call: Call<ResponseBody> = Api.retrofitService.updateTodo(
                  token,
                  UpdateTodo(id, text, dueDate, isDone, isFavored)
              )
              call.enqueue(object : Callback<ResponseBody> {
                  override fun onResponse(
                      call: Call<ResponseBody>,
                      response: Response<ResponseBody>
                  ) {
                      if (response.isSuccessful) {
                          try {
                              val result = JSONObject(response.body()!!.string())
                              val success = result.getBoolean("success")
                              if (success) {
                                  todoList[position].isFavored = "1"
                                  Log.i("sssss", "Yeeeees")
                              } else
                                  Log.i("sssss", "Nooooo")
                              todoAdapter.notifyDataSetChanged()

                          } catch (e: JSONException) {
                              e.printStackTrace()
                          }


                      } else
                          Log.i("sssss", "Error on response")

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
          } catch (e: IOException) {
              e.printStackTrace()
          }
      }*/

}