package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.asteroid.DatabaseAsteroid

@Dao
interface AsteroidDatabaseDao {
    @Insert
    fun insert(asteroid: DatabaseAsteroid)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)
    @Query("SELECT * FROM asteroid_table ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>
    @Query("DELETE FROM asteroid_table")
    fun clear()
    @Query("SELECT COUNT(*) FROM asteroid_table")
    fun getCount() : Int
    @Query("SELECT * FROM asteroid_table WHERE id = :key")
    fun get(key : Long ): DatabaseAsteroid?
    @Update
    fun update(asteroid: DatabaseAsteroid)
}