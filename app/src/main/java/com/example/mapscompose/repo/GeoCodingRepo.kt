package com.example.mapscompose.repo

import com.example.mapscompose.api.GeoCodingApi
import com.example.mapscompose.models.address.AddressResult
import com.example.mapscompose.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeoCodingRepo @Inject constructor(private val geoCodingApi: GeoCodingApi) {

    suspend fun getLocationDataByLatLng(latlng: String): Resource<String> {
        return try{
            val results = geoCodingApi.getLocationDataByLatLng(latlng = latlng).results
            val street = getStreet(results!!)
            val area = getArea(results)
            val block = getBlock(results)

            Resource.Success("$street$block$area")
        }
        catch (e: Exception){
            Resource.Error(e.message!!)
        }


    }

    private fun getStreet(addressResults:List<AddressResult>):String{
        for(result in addressResults){
            for(i in result.address_components!!){
                if(i.types.contains("route")  || i.types.contains("street_address")){
                    if(!i.short_name?.contains("Unnamed Road")!!) {
                        return "${i.short_name}, "
                    }
                }
            }
        }
        return "Unknown Street, "
    }

    private fun getArea(results:List<AddressResult>):String{
        for(result in results){
            for(i in result.address_components!!){
                if(i.types.contains("locality")  || i.types.contains("sublocality") ||
                    i.types.contains("sublocality_level_1") || i.types.contains("sublocality_level_2") ||
                    i.types.contains("sublocality_level_3") || i.types.contains("sublocality_level_4") ||
                    i.types.contains("sublocality_level_5")){
                    return i.short_name!!
                }
            }
        }
        return results[0].address_components[0].short_name!!
    }

    private fun getBlock(results:List<AddressResult>):String{
        for(result in results){
            for(i in result.address_components){
                if(i.types.contains("neighborhood")){
                    return "${i.short_name}, "
                }
            }
        }
        return ""
    }

}