package com.amit.radiuscompose.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exclusion_rules")
data class ExclusionEntity(
    @PrimaryKey
    val id: Int
)