package com.lbweather.getweatherfromall.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [LocationTable::class], version = 2)
abstract class MyDataBase : RoomDatabase() {
    abstract fun locationDao(): MyDao

    companion object {
        const val DATABASE_NAME = "lbweather-database-weather-searcher"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE locationTable ADD COLUMN status_active INTEGER NOT NULL DEFAULT 0")
            }
        }
    }

}