package com.example.mapscompose.models.address

data class AddressResult(
    val address_components: List<AddressComponent>,
    var formatted_address: String?=null,
    val types:List<String>?=null,
)