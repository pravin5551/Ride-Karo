package com.froyo.ridekaro.views.navDrawerFragments

import android.app.FragmentTransaction
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.froyo.ridekaro.R
import com.froyo.ridekaro.viewModel.RidesViewModel
import com.froyo.ridekaro.views.LocationSearchFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_rider_arrived.*

class RiderArrived : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rider_arrived)
        btnLetsRide.setOnClickListener {
            finish()
        }
    }
}