package com.amit.radiuscompose.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facilities")
data class FacilityEntity(
    @PrimaryKey
    val facilityId: String,
    val name: String
)