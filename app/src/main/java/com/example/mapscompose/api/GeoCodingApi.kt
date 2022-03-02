package com.example.mapscompose.api

import com.example.mapscompose.BuildConfig
import com.example.mapscompose.models.address.GeocodingResponse
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

interface GeoCodingApi {


    @POST("geocode/json")
    suspend fun getLocationDataByLatLng(
        @Query("latlng") latlng: String,
        @Query("language") language: String = Locale.getDefault().language,
        @Query("key") key:String = BuildConfig.API_KEY,
    ): GeocodingResponse

}