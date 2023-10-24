package com.festago.festago.data.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface FestivalDao {
    @Query("SELECT * FROM festivals")
    fun getFestivals(): List<FestivalEntity>
}
