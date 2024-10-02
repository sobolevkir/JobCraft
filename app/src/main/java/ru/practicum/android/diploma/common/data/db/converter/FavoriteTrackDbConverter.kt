package ru.practicum.android.diploma.common.data.db.converter

import androidx.room.TypeConverter

class FavoriteTrackDbConverter {
    @TypeConverter
    fun testConverter(text: String): String {
        return text + ""
    } // это конвертер-заглушка
}
