package com.udacity.asteroidradar.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.data.dto.DBAsteroid
import com.udacity.asteroidradar.data.dto.DBPictureOfDay

@Database(
    entities = [DBAsteroid::class, DBPictureOfDay::class],
    version = 1,
    exportSchema = false
)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract fun asteroidDao(): AsteroidDao

    companion object {
        @Volatile
        private var INSTANCE: AsteroidDatabase? = null

        fun getInstance(context: Context): AsteroidDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room
                .databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabase::class.java,
                    "Asteroid.db"
                )
                .fallbackToDestructiveMigration()
                .build()
    }
}
