package com.amit.radiuscompose.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.amit.radiuscompose.model.entity.FacilityEntity
import com.amit.radiuscompose.model.entity.FacilityOptionEntity
import com.amit.radiuscompose.model.entity.FacilityWithOptions

@Dao
interface FacilityDao {
    @Insert
    @Transaction
    fun insertFacilitiesWithOptions(
        facilities: List<FacilityEntity>,
        options: List<FacilityOptionEntity>
    )

    @Transaction
    @Query("SELECT * FROM facilities")
    fun getFacilitiesWithOptions(): List<FacilityWithOptions>

    @Query("DELETE FROM exclusion_rules")
    fun deleteAllExclusionRules()

}