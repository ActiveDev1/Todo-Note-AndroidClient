package com.activedev.todo_note.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "reminder_table", indices = [Index(value = ["remindId"], unique = true)])
class Reminder(
    @NonNull
    @ColumnInfo(name = "remindId")
    var remindId: String,

    @NonNull
    @ColumnInfo(name = "text")
    var text: String,

    @NonNull
    @ColumnInfo(name = "due_date")
    var dueDate: Date?,

    @NonNull
    @ColumnInfo(name = "created_at")
    var createdAt: Date?,

    @NonNull
    @ColumnInfo(name = "updated_at")
    var updatedAt: Date?,

    @NonNull
    @ColumnInfo(name = "is_done")
    var isDone: String,

    @NonNull
    @ColumnInfo(name = "is_favored")
    var isFavored: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
