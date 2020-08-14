package com.activedev.todo_note.network

import android.content.Context
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import com.activedev.todo_note.database.Reminder
import com.activedev.todo_note.database.ReminderDatabase
import kotlinx.android.parcel.Parcelize
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class TodoRequest(
    var context: Context,
    var token: String?
) {
    val reminderDao = ReminderDatabase.getInstance(context).reminderDatabaseDao

    val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())


    fun allTodo() {
        try {
            Log.v("sssss", "Get all todo ...")

            val call: Call<ResponseBody> = Api.retrofitService.getTodo(token)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        reminderDao.clear()
                        Log.v("sssss", "Success")

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

                                    val date: Date? = formatter.parse(dueDate)
                                    val create: Date? = formatter.parse(createdAt)
                                    val update: Date? = formatter.parse(updatedAt)
                                    val reminder =
                                        Reminder(id, text, date, create, update, isDone, isFavored)
                                    reminderDao.insert(reminder)

                                }
                                Log.v("sssss", "Done")

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

    fun createTodo(
        text: String,
        dueDate: String,
        isDone: String,
        isFavored: String
    ) {

        try {
            Log.v("sssss", "Create todo ...")

            val call: Call<ResponseBody> = Api.retrofitService.createTodo(
                token,
                CreateTodo(text, dueDate, isDone, isFavored)
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
                                Log.v("sssss", "Done")
//                                val time: Date = Calendar.getInstance().time
//                                val reminderId = reminderDao.lastReminderId().toInt() + 1
//                                val reminder =
//                                    Reminder(
//                                        reminderId.toString(),
//                                        text,
//                                        formatter.parse(dueDate),
//                                        time,
//                                        time,
//                                        isDone,
//                                        isFavored
//                                    )
//                                reminderDao.insert(reminder)
                            } else
                                Log.v("sssss", "not success")

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


    fun updateTodo(
        id: String,
        text: String,
        dueDate: String,
        isDone: String,
        isFavored: String
    ) {

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
                                Log.v("sssss", "Done")

                            } else {
                                Log.v("sssss", "not success")
                            }

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

@Parcelize
data class UpdateTodo(
    val id: String,
    val text: String,
    val due_date: String,
    val is_done: String,
    val is_favored: String
) : Parcelable

@Parcelize
data class CreateTodo(
    val text: String,
    val due_date: String,
    val is_done: String,
    val is_favored: String
) : Parcelable

