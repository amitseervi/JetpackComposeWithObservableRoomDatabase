package com.amit.radiuscompose.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    "exclusion_options",
    primaryKeys = ["facilityId", "optionsId"],
    foreignKeys = [
        ForeignKey(
            entity = FacilityOptionEntity::class,
            parentColumns = ["id"],
            childColumns = ["optionsId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FacilityEntity::class,
            parentColumns = ["facilityId"],
            childColumns = ["facilityId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExclusionEntity::class,
            parentColumns = ["id"],
            childColumns = ["exclusionSetId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class ExclusionOptionsEntity(
    val exclusionSetId: Int,
    val facilityId: String,
    val optionsId: String
)