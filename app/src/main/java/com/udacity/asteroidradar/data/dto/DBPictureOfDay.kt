package com.udacity.asteroidradar.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* Primarykey on mediaType to make sure only one data is being saved.
* Because mediaType in NASA api = "Image"
* */
@Entity(tableName = "picture_of_day_table")
data class DBPictureOfDay (
    @PrimaryKey val mediaType: String,
    val title: String,
    val url: String)