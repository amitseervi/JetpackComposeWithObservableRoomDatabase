package com.amit.radiuscompose.ui.viewmodels.states.vm

import com.amit.radiuscompose.model.response.FacilitiesData

data class HomeViewModelState(
    val loading: Boolean = false,
    val error: String? = null,
    val errorCode: Int? = null,
    val facilityData: FacilitiesData? = null,
    val selectedOptionsForFacility: Map<String, String> = emptyMap()
)