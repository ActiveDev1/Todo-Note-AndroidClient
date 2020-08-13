package com.activedev.todo_note.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*


@Dao
interface ReminderDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(reminder: Reminder)

    @Query("UPDATE reminder_table SET text = :text, due_date = :dueDate, created_at = :createdAt, updated_at = :updatedAt, is_done = :isDone, is_favored = :isFavored WHERE remindId = :remindId")
    fun update(
        remindId: String,
        text: String,
        dueDate: Date?,
        createdAt: Date?,
        updatedAt: Date?,
        isDone: String,
        isFavored: String
    )

    @Query("UPDATE reminder_table SET is_favored = :isFavored WHERE remindId = :remindId")
    fun updateFavored(
        remindId: String,
        isFavored: String
    )

    @Query("DELETE FROM reminder_table")
    fun clear()

    @Query("SELECT * FROM reminder_table")
    fun getAllTodo(): List<Reminder>

    @Query("SELECT * FROM reminder_table ORDER BY due_date LIMIT 1")
    fun getTodo(): Reminder

    @Query("SELECT remindId FROM reminder_table ORDER BY remindId DESC LIMIT 1")
    fun lastReminderId(): String

}