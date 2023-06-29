package com.amit.radiuscompose.model.response

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class FacilitiesData(
    @field:Json(name = "facilities")
    val facilities: List<FacilityItem>,
    @field:Json(name = "exclusions")
    val exclusion: List<List<ExclusionRuleSetItem>>
)

