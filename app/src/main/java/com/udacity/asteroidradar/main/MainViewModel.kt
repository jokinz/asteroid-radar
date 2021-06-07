package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.asteroid.Asteroid
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel (val database : AsteroidDatabaseDao, application: Application): AndroidViewModel(application) {
//class MainViewModel : ViewModel() {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    private val _asteroidsList = MutableLiveData<List<Asteroid>>()
    val asteroidsList: LiveData<List<Asteroid>>
        get() = _asteroidsList

    val asteroids = database.getAllAsteroids()

    init {
        getAsteroids()
        getImageOfTheDay()
    }


    private fun getAsteroids() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var listResult = parseAsteroidsJsonResult(JSONObject(AsteroidApi.retrofitService.getAsteroidString()))
//                Log.i("getAsteroids", "JSON: " + AsteroidApi.retrofitService.getProperties())
                Log.i("getAsteroids", "Internet list size: " + listResult.size)
//                database.clear()
                saveAsteroidInDatabase(listResult)
                Log.i("getAsteroids", "Database list size: " + database.getCount())
            } catch (e: Exception) {
//                _response.value = "failed"
//                Log.i("getAsteroids", "String: " + response.raw().request().url())
                Log.i("getAsteroids", "Failed: " + e.message)
            }
        }
    }

    private suspend fun saveAsteroidInDatabase(listResult: ArrayList<Asteroid>) {
        for (item in listResult) {
            database.insert(item)
//            Log.i("getAsteroidsLoop", item.codename)
        }
    }

    private fun getImageOfTheDay(){
        viewModelScope.launch {
            try {
                var picture = AsteroidApi.retrofitService.getPictureOfDayString()
                Log.i("getImage", "Image data: " + picture)
            }catch (e: Exception){
                Log.i("getImage", "Error: " + e.message)

            }
        }
    }
}