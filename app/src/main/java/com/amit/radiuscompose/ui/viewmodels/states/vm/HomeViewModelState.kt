package com.amit.radiuscompose.ui.viewmodels.states.vm

import com.amit.radiuscompose.model.entity.FacilityOptionSelectionEntity
import com.amit.radiuscompose.model.response.ExclusionRuleSetItem
import com.amit.radiuscompose.model.response.FacilityItem

data class HomeViewModelState(
    val loading: Boolean = false,
    val error: String? = null,
    val errorCode: Int? = null,
    val facilities: List<FacilityItem> = emptyList(),
    val exclusion: List<List<ExclusionRuleSetItem>> = emptyList(),
    val userPreference: List<Pair<String, String>> = emptyList()
) {
}

data class HomeViewModelStateSelection(
    val facilityId: String,
    val optionId: String
)