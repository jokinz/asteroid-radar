package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.asteroid.Asteroid
import com.udacity.asteroidradar.asteroid.DatabaseAsteroid
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.database.getInstance
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.asDomainModel
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel (application: Application): AndroidViewModel(application) {
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

//class MainViewModel : ViewModel() {
//    private val _asteroidList = MutableLiveData<List<Asteroid>>()
//
//    val asteroidList: LiveData<List<Asteroid>>
//        get() = _asteroidList

//    val asteroids = asteroidDatabaseDao.getAllAsteroids()

    private val database = getInstance(application)
    private val asteroidsRepository = AsteroidRepository(database)

    init {
        viewModelScope.launch{
            asteroidsRepository.refreshAsteroids()
        }
        getImageOfTheDay()
    }

    val asteroidList = asteroidsRepository.asteroids

//    private fun getAsteroids() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                var listResult = AsteroidApi.retrofitService.getAsteroidList().await()
//                _asteroidList.postValue(listResult.asDomainModel())
//            } catch (e: Exception) {
//            }
//        }
//    }


    private fun getImageOfTheDay(){
        viewModelScope.launch {
            try {
                var picture = AsteroidApi.retrofitService.getPictureOfDayString()
                Log.i("getImage", "Image data: " + picture.url)
                _pictureOfDay.value = picture
            }catch (e: Exception){
                Log.i("getImage", "Error: " + e.message)
            }
        }
    }

    private val _triggerDetails = MutableLiveData<DatabaseAsteroid>()
    val triggerDetails
        get() = _triggerDetails

    fun onAsteroidClick(asteroid: DatabaseAsteroid){
        _triggerDetails.value = asteroid
    }

    fun onAsteroidClicked() {
        _triggerDetails.value = null
    }

}