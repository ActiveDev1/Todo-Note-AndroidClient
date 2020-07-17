package com.activedev.todo_note.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserRequest(
    val name: String,
    val username: String,
    val email: String,
    val password: String
) : Parcelable
