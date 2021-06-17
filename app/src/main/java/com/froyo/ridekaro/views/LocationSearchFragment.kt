package com.froyo.ridekaro.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.froyo.ridekaro.R
import kotlinx.android.synthetic.main.fragment_location_search.*

class LocationSearchFragment : Fragment(R.layout.fragment_location_search) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)

        btnSearchLocation.setOnClickListener {
            val address = etSearchLocation.text.toString()
            if (address != "") {
                locationViewModel.addLocation(address)
                activity?.onBackPressed()
            } else {
                etSearchLocation.error = "Please enter area"
            }
        }
    }
}