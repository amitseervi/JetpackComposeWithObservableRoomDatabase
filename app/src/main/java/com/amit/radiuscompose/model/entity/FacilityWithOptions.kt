package com.amit.radiuscompose.model.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class FacilityWithOptions(
    @Embedded
    val facility: FacilityEntity,
    @Relation(
        entity = FacilityOptionEntity::class,
        parentColumn = "facilityId",
        entityColumn = "facilityId",
    )
    val options: List<FacilityOptionEntity>
)