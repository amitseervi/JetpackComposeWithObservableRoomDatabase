package com.amit.radiuscompose.model.network.service

import com.amit.radiuscompose.model.network.CustomHeaders
import com.amit.radiuscompose.model.response.FacilitiesData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers

private const val TTL_ONE_DAY: Long = 24 * 60 * 60 * 1000

interface RadiusService {
    @GET("/iranjith4/ad-assignment/db")
    @Headers(
        value = [
            "${CustomHeaders.CACHE_KEY}: facility_network_cache",
            "${CustomHeaders.CACHE_TTL}: $TTL_ONE_DAY"
        ]
    )
    suspend fun getFacilitiesData(): Response<FacilitiesData>
}