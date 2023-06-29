package com.amit.radiuscompose.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_pref_facility_option",
)
class FacilityOptionSelectionEntity(
    @PrimaryKey(autoGenerate = false)
    val facilityId: String,
    val optionId: String
)