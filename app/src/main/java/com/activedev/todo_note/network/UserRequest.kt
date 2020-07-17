package com.activedev.todo_note.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//class UserRequest(var name:String,var username: String, var email: String, var password: String)

@Parcelize
data class UserRequest(
    val name: String,
    val username: String,
    val email: String,
    val password: String
) : Parcelable

class UserResult(var success: String)