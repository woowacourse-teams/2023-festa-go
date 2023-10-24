package com.festago.festago.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FestivalDao {
    @Query("SELECT * FROM festivals")
    fun getFestivals(): Flow<List<FestivalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFestivals(festivals: List<FestivalEntity>): List<Long>
}
