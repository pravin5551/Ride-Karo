package com.froyo.ridekaro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RidesViewModel : ViewModel() {

    companion object {
        var ride: MutableLiveData<Int> = MutableLiveData()
    }

    fun openRiderFragment(distance: Int) {
        AfterClickingRideNow.ride.value = distance
    }

    fun setRiderFragment(): LiveData<Int> {
        return AfterClickingRideNow.ride
    }
}