package com.amit.radiuscompose.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amit.radiuscompose.model.dao.FacilityDao
import com.amit.radiuscompose.model.entity.FacilityOptionSelectionEntity

@Database(
    entities = [
        FacilityOptionSelectionEntity::class
    ],
    version = 1
)
abstract class RadiusDB : RoomDatabase() {

    abstract fun facilityDao(): FacilityDao

    companion object {
        const val DB_NAME = "RadiusDB"
    }
}