package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.common.data.db.entity.FavoriteVacancyEntity

@Dao
interface FavoriteVacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteVacancyEntity)
    @Query("SELECT * FROM favorite_vacancy_table")
    suspend fun getTracks(): List<FavoriteVacancyEntity>
}
