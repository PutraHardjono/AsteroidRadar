package com.udacity.asteroidradar.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* Class for database in Room
* */
@Entity(tableName = "asteroid_table")
data class DBAsteroid(
    @PrimaryKey val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)