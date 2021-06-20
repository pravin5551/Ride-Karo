package com.froyo.ridekaro.views

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {

    companion object {
        var address: MutableLiveData<String> = MutableLiveData()
    }

    fun addLocation(location: String) {
        address.value = location
    }

    fun getLocation(): MutableLiveData<String> {
        return address
    }


}