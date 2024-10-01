package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.db.entity.FavoriteVacancyEntity

@Dao
interface FavoriteVacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(track: FavoriteVacancyEntity)
    @Query("SELECT * FROM favorite_vacancy_table")
    fun getVacancys(): Flow<List<FavoriteVacancyEntity>>
    @Query("SELECT id FROM favorite_vacancy_table")
    fun getFavoritesIds(): Flow<List<String>>
}
