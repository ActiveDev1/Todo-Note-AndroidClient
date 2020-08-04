package com.activedev.todo_note.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


private const val BASE_URL = "http://192.168.1.36//Todo-Note/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("user/register")
    fun createUser(@Body body: CreateUserRequest?): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("user/login")
    fun loginUser(@Body body: LoginUserRequest?): Call<ResponseBody>

    @GET("todo")
    fun getTodo(@Header("Authorization") auth: String?): Call<ResponseBody>

    @PATCH("todo")
    fun updateTodo(
        @Header("Authorization") auth: String?,
        @Body body: UpdateTodo
    ): Call<ResponseBody>

}

object Api {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
