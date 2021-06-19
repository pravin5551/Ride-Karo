package com.froyo.ridekaro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class LatLongViewModel : ViewModel() {
    companion object {
        var location: MutableLiveData<LatLng> = MutableLiveData()
    }

    fun setLocation(latLng: LatLng) {
        LatLongViewModel.location.value = latLng
    }

    fun getLocation(): LiveData<LatLng> {
        return LatLongViewModel.location
    }
}