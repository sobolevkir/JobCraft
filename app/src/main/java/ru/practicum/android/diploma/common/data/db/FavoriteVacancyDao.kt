package ru.practicum.android.diploma.common.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.data.entity.FavoriteVacancyEntity

@Dao
interface FavoriteVacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: FavoriteVacancyEntity)

    @Query("DELETE FROM favorite_vacancy_table WHERE id = :vacancyId")
    suspend fun removeVacancy(vacancyId: Long)

    @Update
    suspend fun updateVacancy(vacancy: FavoriteVacancyEntity)

    @Query("SELECT * FROM favorite_vacancy_table")
    fun getVacancies(): Flow<List<FavoriteVacancyEntity>>

    @Query("SELECT id FROM favorite_vacancy_table")
    fun getFavoritesIds(): Flow<List<Long>>

    @Query("DELETE FROM favorite_vacancy_table WHERE id = :vacancyId")
    suspend fun deleteVacancyById(vacancyId: Long)

    @Update(entity = FavoriteVacancyEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateVacancy(playlist: FavoriteVacancyEntity)

    @Query("SELECT id FROM favorite_vacancy_table")
    suspend fun getFavoritesIdsList(): List<Long>

    @Query("SELECT * FROM favorite_vacancy_table WHERE id = :vacancyId")
    suspend fun getVacancy(vacancyId: Long): FavoriteVacancyEntity
}
