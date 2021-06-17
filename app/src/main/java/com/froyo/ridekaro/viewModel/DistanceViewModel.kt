package com.froyo.ridekaro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DistanceViewModel : ViewModel() {

    companion object {
        var length: MutableLiveData<Float> = MutableLiveData()
    }

    fun addDistance(distance: Float) {
        length.value = distance
    }

    fun getDistance(): LiveData<Float> {
        return length
    }
}