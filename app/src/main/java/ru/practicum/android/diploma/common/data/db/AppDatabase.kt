package ru.practicum.android.diploma.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.practicum.android.diploma.common.data.db.converter.FavoriteTrackDbConverter
import ru.practicum.android.diploma.common.data.db.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.common.data.db.entity.FavoriteVacancyEntity

@Database(version = 1, entities = [FavoriteVacancyEntity::class])
@TypeConverters(FavoriteTrackDbConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteVacancysDao(): FavoriteVacancyDao
}
