package com.lbweather.getweatherfromall.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM locationTable")
    fun getAll(): Flow<List<LocationTable>>

    @Query("SELECT * FROM locationTable WHERE status = 1")
    fun getActiveLocation(): List<LocationTable>

    @Query("UPDATE locationTable SET status = 0")
    fun resetLocationStatus()

    @Update
    fun update(locationTable: LocationTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locationTable: LocationTable)

    @Delete
    fun delete(locationTable: LocationTable)
}