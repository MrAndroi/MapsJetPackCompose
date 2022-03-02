package com.example.mapscompose.presentaion.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapscompose.repo.GeoCodingRepo
import com.example.mapscompose.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val geoCodingRepo: GeoCodingRepo):ViewModel() {

    var currentLocationInfo = mutableStateOf("Please select your location")
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    fun getLocationInfo(latLng: String) = viewModelScope.launch {
        isLoading.value = true
        val response = geoCodingRepo.getLocationDataByLatLng(latLng)
        Log.e("Response","${response.message}")
        when(response){
            is Resource.Success ->{
                currentLocationInfo.value = response.data ?: "Error"
                isLoading.value = false
                loadError.value = ""
            }
            is Resource.Error ->{
                loadError.value = response.message!!
                isLoading.value = false
            }
        }
    }

}