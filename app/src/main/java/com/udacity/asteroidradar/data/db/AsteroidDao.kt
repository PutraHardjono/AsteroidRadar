package com.udacity.asteroidradar.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.data.dto.DBAsteroid
import com.udacity.asteroidradar.data.dto.DBPictureOfDay

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(adAsteroidList: List<DBAsteroid>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApod(dbPictureOfDay: DBPictureOfDay)

    @Query("SELECT * FROM picture_of_day_table ORDER BY mediaType DESC LIMIT 1")
    fun getApod(): LiveData<PictureOfDay>

    @Query("SELECT * FROM asteroid_table ORDER BY closeApproachDate ASC")
    fun getAllAsteroid(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid_table WHERE DATE(closeApproachDate) BETWEEN :fromDate AND :toDate ORDER BY closeApproachDate ASC")
    fun getBetweenDate(fromDate: String, toDate: String): LiveData<List<Asteroid>>

    @Query("DELETE FROM asteroid_table WHERE DATE(closeApproachDate) < DATE('now')")
    fun deletePastAsteroid()
}