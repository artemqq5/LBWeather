package com.lbweather.getweatherfromall.data.database

import androidx.room.*

@Dao
interface MyDao {
    @Query("SELECT * FROM locationTable")
    fun getAll(): List<LocationTable>

    @Update
    fun update(locationTable: LocationTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locationTable: LocationTable)

    @Delete
    fun delete(locationTable: LocationTable)
}