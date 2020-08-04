package com.activedev.todo_note.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.activedev.todo_note.adapters.TodoAdapter
import com.activedev.todo_note.model.Todo
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Request(
    var context: Context?,
    var token: String?,
    var todo: MutableList<Todo>
) {

    fun allTodo(todoAdapter: TodoAdapter) {
        val token = token
        try {
            Log.v("sssss", "Get all todo ...")

            val call: Call<ResponseBody> = Api.retrofitService.getTodo(token)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()!!.string()
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
                                Log.v("sssss", "Done")

                                todoAdapter.notifyDataSetChanged()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    /*  FancyToast.makeText(
                          context,
                          "Error on connection !!",
                          FancyToast.LENGTH_SHORT,
                          FancyToast.ERROR, false
                      ).show()*/
                    Log.v("sssss", "Error on connect")

                }
            })
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    fun updateTodo(
        item: Todo,
        id: String,
        text: String,
        dueDate: String,
        isDone: String,
        isFavored: String
    ) {

        val s = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val format: String = s.format(Date())
//        Log.v("sssss", format)
        try {
            Log.v("sssss", "Updating todo ...")

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
                                Log.v("sssss", "success")
                            } else
                                Log.v("sssss", "not success")
//                            todoAdapter.notifyDataSetChanged()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else
                        Log.v("sssss", "Error on response")

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    FancyToast.makeText(
//                        context,
//                        "Error on connection !!",
//                        FancyToast.LENGTH_SHORT,
//                        FancyToast.ERROR, false
//                    ).show()
                    Log.v("sssss", "Error on connect")

                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}