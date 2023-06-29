package com.amit.radiuscompose.model.response

import com.squareup.moshi.Json

data class FacilitiesData(
    @field:Json(name = "facilities")
    val facilities: List<FacilityItem>,
    @field:Json(name = "exclusions")
    val exclusion: List<List<ExclusionRuleSetItem>>
)

data class FacilityItem(
    @field:Json(name = "facility_id")
    val facilityId: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "options")
    val options: List<FacilityOption>
)

data class FacilityOption(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "icon")
    val icon: String,
    @field:Json(name = "id")
    val id: String
)

data class ExclusionRuleSetItem(
    @field:Json(name = "facility_id")
    val facilityId: String,
    @field:Json(name = "options_id")
    val optionId: String
)