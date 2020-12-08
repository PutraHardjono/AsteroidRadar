package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.util.Injection

class LoadDataWorker (appContext: Context, params: WorkerParameters)
    : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "LoadDataWorker"
    }

    override suspend fun doWork(): Result {
        val repo = Injection.provideAsteroidRepo(applicationContext)
        val isSuccess = repo.refreshNeoWs()

        // BONUS TASK 2: Delete asteroids from previous day
        repo.deletePastAsteroid()

        return if (isSuccess) Result.success()
        else Result.retry()
    }
}