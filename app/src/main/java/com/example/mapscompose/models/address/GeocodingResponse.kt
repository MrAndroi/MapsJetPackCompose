package com.example.mapscompose.models.address

data class GeocodingResponse(
    val results: List<AddressResult>,
    val plus_code: PlusCode,
)