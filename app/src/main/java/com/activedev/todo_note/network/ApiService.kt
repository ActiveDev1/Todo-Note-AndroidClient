package com.activedev.todo_note.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


private const val BASE_URL = "http://192.168.1.36:2080//TODO-Note/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("user/register")
    fun postJson(@Body body: UserRequest?): Call<ResponseBody>
}

object Api {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
