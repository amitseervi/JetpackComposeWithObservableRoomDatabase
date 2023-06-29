package com.amit.radiuscompose.model.entity

import androidx.room.Entity

@Entity(tableName = "facilities")
data class FacilityEntity(
    val facilityId: String,
    val name: String
)