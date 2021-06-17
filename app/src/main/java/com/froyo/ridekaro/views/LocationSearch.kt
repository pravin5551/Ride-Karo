package com.froyo.ridekaro.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.froyo.ridekaro.R
import com.froyo.ridekaro.viewModel.MyViewModel
import com.froyo.ridekaro.views.navDrawerFragments.HomeFragment
import kotlinx.android.synthetic.main.activity_location_search.*

class LocationSearch : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_search)
        btnSerchLocation.setOnClickListener {
            val address = etSearchLocation.text.toString()
            if (address != "") {
                finish()
            } else {
                etSearchLocation.error = "Please enter area"
            }
        }
    }
}