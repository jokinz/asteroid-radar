package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.asteroid.Asteroid
import com.udacity.asteroidradar.asteroid.asDomainModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidRepository(private  val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDatabaseDao.getAllAsteroids()){
        it.asDomainModel()
    }
    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
            val asteroidsList = AsteroidApi.retrofitService.getAsteroidList().await()
            database.asteroidDatabaseDao.insertAll(*asteroidsList.asDatabaseModel())
        }
    }
}