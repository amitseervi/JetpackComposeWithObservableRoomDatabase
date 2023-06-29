package com.amit.radiuscompose.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ExclusionEntityWithOptions(
    @Embedded
    val exclusion: ExclusionEntity,
    @Relation(
        entity = ExclusionOptionsEntity::class,
        parentColumn = "id",
        entityColumn = "exclusionSetId"
    )
    val options: List<ExclusionOptionsEntity>
)