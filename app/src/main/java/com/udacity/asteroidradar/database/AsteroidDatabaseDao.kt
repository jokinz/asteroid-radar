package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.udacity.asteroidradar.asteroid.Asteroid

@Dao
interface AsteroidDatabaseDao {
    @Insert
    fun insert(asteroid: Asteroid)
    @Query("SELECT * FROM asteroid_table ORDER BY id DESC")
    fun getAllAsteroids(): LiveData<List<Asteroid>>
    @Query("DELETE FROM asteroid_table")
    fun clear()
    @Query("SELECT COUNT(*) FROM asteroid_table")
    fun getCount() : Int
    @Query("SELECT * FROM asteroid_table WHERE id = :key")
    fun get(key : Long ): Asteroid?
    @Update
    fun update(asteroid: Asteroid)
}