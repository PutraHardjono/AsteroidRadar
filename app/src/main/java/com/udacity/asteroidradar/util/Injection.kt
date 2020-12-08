package com.udacity.asteroidradar.util

import android.content.Context
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.data.db.AsteroidDatabase
import com.udacity.asteroidradar.repo.AsteroidRepo

object Injection {
    fun provideAsteroidRepo(context: Context): AsteroidRepo {
        return AsteroidRepo(NasaApiService.create(), AsteroidDatabase.getInstance(context).asteroidDao())
    }
}