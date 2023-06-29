package com.amit.radiuscompose.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amit.radiuscompose.model.entity.FacilityOptionSelectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FacilityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateUserPreferredFacilityOption(entity: FacilityOptionSelectionEntity)

    @Query("SELECT * from user_pref_facility_option WHERE 1")
    fun getUserFacilityPreferenceOptions(): Flow<List<FacilityOptionSelectionEntity>>

    @Delete(FacilityOptionSelectionEntity::class)
    fun deleteUserPreferredFacilityOption(facilityOptionSelectionEntity: FacilityOptionSelectionEntity)
}