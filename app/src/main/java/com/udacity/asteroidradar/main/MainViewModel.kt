package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.repo.AsteroidRepo
import com.udacity.asteroidradar.util.Show
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val asteroidRepo: AsteroidRepo
) : AndroidViewModel(application) {

    val apod: LiveData<PictureOfDay> get() =  asteroidRepo.getApod()

    private val _asteroids = MediatorLiveData<List<Asteroid>>()
    val asteroids get() = _asteroids

    private val _connection = MutableLiveData<Boolean?>()
    val connection: LiveData<Boolean?> get() = _connection

    init {
        if (isConnected(application.applicationContext)) {
            viewModelScope.launch { asteroidRepo.refreshApod() }
            viewModelScope.launch {
                // If repo failed to launch, then check connection
                if (!asteroidRepo.refreshNeoWs())
                    checkConnection(application.applicationContext)
            }
        }
        else _connection.value = false

        search(Show.WEEKS)
    }

    // Check connection to trigger _connection
    private fun checkConnection(context: Context) {
        if (!isConnected(context))
            _connection.value = false
    }

    fun checkConnectionDone() {
        _connection.value = null
    }

    // get asteroid list base on the show
    fun search(show: Show) {
        _asteroids.addSource(asteroidRepo.getAsteroid(show), _asteroids::setValue)
    }

    // check internet connection
    private fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}

