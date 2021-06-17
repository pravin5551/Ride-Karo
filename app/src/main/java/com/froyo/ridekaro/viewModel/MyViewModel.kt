package com.froyo.ridekaro.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    private var address = MutableLiveData<String>()

    fun getAddress(text: String) {
        address.value = text
    }

    fun sendAddress(): MutableLiveData<String> {
        return address
    }

}