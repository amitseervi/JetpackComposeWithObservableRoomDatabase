package com.amit.radiuscompose.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "facility_options", foreignKeys = [
        ForeignKey(
            entity = FacilityEntity::class,
            parentColumns = ["facilityId"],
            childColumns = ["facilityId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class FacilityOptionEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val names: String,
    val icon: String,
    val facilityId: String
)