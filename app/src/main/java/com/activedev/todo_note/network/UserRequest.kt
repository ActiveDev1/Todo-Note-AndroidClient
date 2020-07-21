package com.activedev.todo_note.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreateUserRequest(
    val name: String,
    val username: String,
    val email: String,
    val password: String
) : Parcelable

@Parcelize
data class LoginUserRequest(
    val usernameOrEmail: String,
    val password: String
) : Parcelable

