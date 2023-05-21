package com.lbweather.myapplication.data.database

import android.view.View
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [LocationTable::class], version = 2)
abstract class MyDataBase : RoomDatabase() {
    abstract fun locationDao(): MyDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE locationTable ADD COLUMN status_active INTEGER NOT NULL DEFAULT ${View.INVISIBLE}")
            }
        }
    }

}