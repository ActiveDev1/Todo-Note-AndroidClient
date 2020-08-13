package com.activedev.todo_note.database

import androidx.room.TypeConverter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateTypeConverter {
    private val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    @TypeConverter
    fun LongtoDateConverter(date: String): Date? {
        return formatter.parse(date)
    }

    @TypeConverter
    fun DatetoLongConverter(date: Date): String {
        return formatter.format(date)
    }
}