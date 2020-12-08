package com.udacity.asteroidradar.repo

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.db.AsteroidDao
import com.udacity.asteroidradar.util.Show
import com.udacity.asteroidradar.util.toDBAsteroid
import com.udacity.asteroidradar.util.toDBPictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.time.LocalDate

class AsteroidRepo(
    private val service: NasaApiService,
    private val asteroidDao: AsteroidDao
) {

    // Cache the asteroid to database
    suspend fun refreshNeoWs(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getNeoWs(LocalDate.now().toString(), BuildConfig.API_KEY)
                if (response.isSuccessful) {
                    val list = parseAsteroidsJsonResult(JSONObject(response.body()!!))
                        .map { it.toDBAsteroid() }
                    asteroidDao.insertAll(list)
                    true
                }
                else {
                    Timber.e("unsuccessful")
                    false
                }
            }
            catch (ex: Exception) {
                Timber.e("$ex")
                false
            }
        }
    }

    // Cache picture of the day to database
    suspend fun refreshApod(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getApod(BuildConfig.API_KEY)
                if (response.isSuccessful) {
                    val apod = response.body()
                    apod?.let { asteroidDao.insertApod(it.toDBPictureOfDay()) }
                    true
                }
                else {
                    Timber.e("unsuccessful")
                    false
                }
            }
            catch (ex: Exception) {
                Timber.e("$ex")
                false
            }
        }
    }

    fun getApod() = asteroidDao.getApod()

    fun getAsteroid(show: Show): LiveData<List<Asteroid>> {
        return when (show) {
            Show.SAVED ->  asteroidDao.getAllAsteroid()
            Show.TODAY -> asteroidDao.getBetweenDate(LocalDate.now().toString(), LocalDate.now().toString())
            Show.WEEKS -> asteroidDao.getBetweenDate(LocalDate.now().toString(), LocalDate.now().plusDays(Constants.DEFAULT_END_DATE_DAYS.toLong()).toString())
        }
    }

    // Delete asteroids from previous days
    fun deletePastAsteroid() = asteroidDao.deletePastAsteroid()
}