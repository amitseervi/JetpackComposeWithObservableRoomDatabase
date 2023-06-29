package com.amit.radiuscompose.model.repo

import com.amit.radiuscompose.model.entity.FacilityOptionSelectionEntity
import kotlinx.coroutines.flow.Flow

interface FacilityUserPreferenceRepository {
    val status: Flow<List<FacilityOptionSelectionEntity>>

    suspend fun saveFacilityOptionSelection(facilityId: String, optionId: String, enable: Boolean)
}