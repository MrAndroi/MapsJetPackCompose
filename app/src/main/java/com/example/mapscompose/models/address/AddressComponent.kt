package com.example.mapscompose.models.address

data class AddressComponent(
    val long_name: String?=null,
    val short_name: String?=null,
    val types: List<String>
)