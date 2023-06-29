package com.amit.radiuscompose.model.response

import com.squareup.moshi.Json

data class FacilityItem(
    @field:Json(name = "facility_id")
    val facilityId: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "options")
    val options: List<FacilityOption>
)