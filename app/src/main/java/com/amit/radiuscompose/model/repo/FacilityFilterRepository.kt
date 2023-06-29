package com.amit.radiuscompose.model.repo

import com.amit.radiuscompose.model.response.FacilitiesData
import com.amit.radiuscompose.model.response.RepositoryStatus
import kotlinx.coroutines.flow.Flow

interface FacilityFilterRepository {
    val status: Flow<RepositoryStatus<FacilitiesData>>

    suspend fun refresh()
}