package com.festago.festago.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.festago.festago.data.dao.RecentSearchQueryDao
import com.festago.festago.data.model.RecentSearchQueryEntity

@Database(entities = [RecentSearchQueryEntity::class], version = 1)
abstract class FestagoDatabase : RoomDatabase() {
    abstract fun recentSearchQueryDao(): RecentSearchQueryDao
}
