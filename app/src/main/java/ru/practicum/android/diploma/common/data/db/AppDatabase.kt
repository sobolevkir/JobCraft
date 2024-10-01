package ru.practicum.android.diploma.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.practicum.android.diploma.common.data.db.converters.Converters
import ru.practicum.android.diploma.common.data.db.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.common.data.db.entity.FavoriteVacancyEntity

@Database(version = 1, entities = [FavoriteVacancyEntity::class])
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteTracksDao(): FavoriteVacancyDao
}
