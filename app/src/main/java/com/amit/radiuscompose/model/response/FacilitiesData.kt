package com.amit.radiuscompose.model.response

import com.squareup.moshi.Json

data class FacilitiesData(
    @Json(name = "facilities")
    val facilities: List<FacilityItem>,
    @Json(name = "exclusions")
    val exclusion: List<List<ExclusionRuleSetItem>>
)

data class FacilityItem(
    @Json(name = "facility_id")
    val facilityId: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "options")
    val options: List<FacilityItemOption>
)

data class FacilityItemOption(
    @Json(name = "name")
    val name: String,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "id")
    val id: String
)

data class ExclusionRuleSetItem(
    @Json(name = "facility_id")
    val facilityId: String,
    @Json(name = "options_id")
    val optionId: String
)