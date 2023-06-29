package com.amit.radiuscompose.model.repo.impl

import com.amit.radiuscompose.model.db.RadiusDB
import com.amit.radiuscompose.model.entity.FacilityOptionSelectionEntity
import com.amit.radiuscompose.model.repo.FacilityUserPreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FacilityUserPreferenceRepositoryImpl @Inject constructor(db: RadiusDB) :
    FacilityUserPreferenceRepository {
    private val dao = db.facilityDao()
    override val status: Flow<List<FacilityOptionSelectionEntity>>
        get() = dao.getUserFacilityPreferenceOptions()

    override suspend fun saveFacilityOptionSelection(
        facilityId: String,
        optionId: String,
        enable: Boolean
    ) =
        withContext(Dispatchers.IO) {
            if (enable) {
                dao.updateUserPreferredFacilityOption(
                    FacilityOptionSelectionEntity(
                        facilityId,
                        optionId
                    )
                )
            }else{
                dao.deleteUserPreferredFacilityOption(
                    FacilityOptionSelectionEntity(
                        facilityId,
                        optionId
                    )
                )
            }
        }
}