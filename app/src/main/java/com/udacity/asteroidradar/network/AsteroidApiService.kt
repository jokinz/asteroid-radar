package com.udacity.asteroidradar.network

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.asteroid.Asteroid
import com.udacity.asteroidradar.asteroid.DatabaseAsteroid
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

val calendar = Calendar.getInstance().timeInMillis
val currentDate = calendar.toString().format(DateTimeFormatter.ofPattern(API_QUERY_DATE_FORMAT))
val limitDate = (calendar+604800000L).toString().format(DateTimeFormatter.ofPattern(API_QUERY_DATE_FORMAT))

interface AsteroidApiService {
    @RequiresApi(Build.VERSION_CODES.O)
    @GET("neo/rest/v1/feed?api_key=" + Constants.API_KEY)
    suspend fun getAsteroidList(@Query("start_date") startDate: String= currentDate, @Query("end_date") endDate: String= limitDate):
            Deferred<NetworkAsteroidContainer>
    @GET("planetary/apod?api_key=" + Constants.API_KEY)
    suspend fun getPictureOfDayString():
            PictureOfDay
}

object AsteroidApi {
    val retrofitService : AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val Asteroids: List<NetworkAsteroid>)

/**
 * Asteroids represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

/**
 * Convert Network results to database objects
 */
fun NetworkAsteroidContainer.asDomainModel(): List<Asteroid> {
    return Asteroids.map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }
}

fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return Asteroids.map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }.toTypedArray()
}