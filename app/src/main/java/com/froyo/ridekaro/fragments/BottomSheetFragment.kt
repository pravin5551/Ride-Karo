package com.froyo.ridekaro.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.froyo.ridekaro.R
import com.froyo.ridekaro.viewModel.DistanceViewModel
import com.froyo.ridekaro.views.LocationViewModel
import com.froyo.ridekaro.views.RiderComing
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*

class BottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val paymentMethodFragment = PaymentMethodFragment()
        bottomRelative3.setOnClickListener {
            paymentMethodFragment.show(parentFragmentManager, paymentMethodFragment.tag)
        }
    }

    override fun onResume() {
        super.onResume()
        val distanceViewModel = ViewModelProviders.of(this).get(DistanceViewModel::class.java)

        distanceViewModel.getDistance().observe(viewLifecycleOwner, Observer {
            val distance = it.toString()
            setAmount(it)
            tvTotalDistance.text = distance + " KM"
        })

        btnRequestRide.setOnClickListener {
            startActivity(Intent(context, RiderComing::class.java))
        }
    }

    private fun setAmount(distance: Float) {
        val totalAmount: Int = (distance * 10).toInt()
        tvTotalRupee.text = totalAmount.toString()
    }

}