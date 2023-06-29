package com.amit.radiuscompose.model.response

import com.squareup.moshi.Json

data class FacilityOption(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "icon")
    val icon: String,
    @field:Json(name = "id")
    val id: String
)