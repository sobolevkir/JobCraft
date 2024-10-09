package ru.practicum.android.diploma.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.favorites.data.entity.FavoriteVacancyEntity

@Database(version = 3, entities = [FavoriteVacancyEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun getFavoriteVacanciesDao(): FavoriteVacancyDao
}
