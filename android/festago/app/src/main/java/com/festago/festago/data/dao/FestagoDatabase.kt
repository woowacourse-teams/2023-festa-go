package com.festago.festago.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        FestivalEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class FestagoDatabase : RoomDatabase() {
    abstract fun festivalDao(): FestivalDao

    companion object {
        private const val DATABASE_NAME = "festago.db"

        fun buildDatabase(context: Context): FestagoDatabase {
            return Room.databaseBuilder(
                context,
                FestagoDatabase::class.java,
                DATABASE_NAME,
            ).build()
        }
    }
}
