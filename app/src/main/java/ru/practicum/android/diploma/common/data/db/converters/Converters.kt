package ru.practicum.android.diploma.common.data.db.converters

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun testConverter(text: String): String {
        return text + ""
    } // это конвертер-заглушка
}
