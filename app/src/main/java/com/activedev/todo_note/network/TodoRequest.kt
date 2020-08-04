package com.activedev.todo_note.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateTodo(
    val id: String,
    val text: String,
    val due_date: String,
    val is_done: String,
    val is_favored: String
) : Parcelable