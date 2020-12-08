package com.udacity.asteroidradar.util

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.data.dto.DBAsteroid
import com.udacity.asteroidradar.data.dto.DBPictureOfDay

fun Asteroid.toDBAsteroid(): DBAsteroid {
    return DBAsteroid(
        id = this.id,
        codename = this.codename,
        closeApproachDate = this.closeApproachDate,
        absoluteMagnitude = this.absoluteMagnitude,
        estimatedDiameter = this.estimatedDiameter,
        relativeVelocity = this.relativeVelocity,
        distanceFromEarth = this.distanceFromEarth,
        isPotentiallyHazardous = this.isPotentiallyHazardous
    )
}

fun PictureOfDay.toDBPictureOfDay(): DBPictureOfDay {
    return DBPictureOfDay(
        title = this.title,
        mediaType = this.mediaType,
        url = this.url
    )
}

