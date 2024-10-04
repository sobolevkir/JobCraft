package ru.practicum.android.diploma.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.practicum.android.diploma.common.data.db.converter.FavoriteVacancyDbConverter
import ru.practicum.android.diploma.common.data.db.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.common.data.db.entity.FavoriteVacancyEntity

@Database(version = 1, entities = [FavoriteVacancyEntity::class])
@TypeConverters(FavoriteVacancyDbConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getFavoriteVacanciesDao(): FavoriteVacancyDao
}
