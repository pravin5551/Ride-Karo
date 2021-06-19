package com.froyo.ridekaro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AfterClickingRideNow : ViewModel() {
    companion object {
        var ride: MutableLiveData<Int> = MutableLiveData()
    }

    fun setMapRider(distance: Int) {
        AfterClickingRideNow.ride.value = distance
    }

    fun getMapRider(): LiveData<Int> {
        return AfterClickingRideNow.ride
    }

}