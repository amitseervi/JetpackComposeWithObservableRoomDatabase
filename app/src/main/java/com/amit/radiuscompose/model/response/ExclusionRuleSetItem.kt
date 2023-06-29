package com.amit.radiuscompose.model.response

import com.squareup.moshi.Json

data class ExclusionRuleSetItem(
    @field:Json(name = "facility_id")
    val facilityId: String,
    @field:Json(name = "options_id")
    val optionId: String
)