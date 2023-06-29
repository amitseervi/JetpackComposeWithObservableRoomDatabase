package com.amit.radiuscompose.model.repo.impl

import com.amit.radiuscompose.model.db.RadiusDB
import com.amit.radiuscompose.model.network.service.RadiusService
import com.amit.radiuscompose.model.repo.FacilityFilterRepository
import com.amit.radiuscompose.model.response.FacilitiesData
import com.amit.radiuscompose.model.response.RepositoryStatus
import com.amit.radiuscompose.model.response.RepositoryStatus.Error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FacilityFilterRepositoryImpl @Inject constructor(
    db: RadiusDB,
    private val service: RadiusService
) : FacilityFilterRepository {
    private val facilityDao = db.facilityDao()
    private val mStatus: MutableStateFlow<RepositoryStatus<FacilitiesData>> =
        MutableStateFlow(RepositoryStatus.Loading())

    override val status: Flow<RepositoryStatus<FacilitiesData>>
        get() = mStatus

    override suspend fun refresh() {
        mStatus.update {
            RepositoryStatus.Loading()
        }
        val networkResponse = service.getFacilitiesData()
        val result = if (networkResponse.isSuccessful) {
            networkResponse.body().let { data ->
                if (data == null) {
                    Error("Null Data received", 200)
                } else {
                    RepositoryStatus.Success(data)
                }
            }
        } else {
            Error(networkResponse.message(), networkResponse.code())
        }
        mStatus.update { result }
    }
}