package com.froyo.ridekaro.views.navDrawerFragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.froyo.ridekaro.R
import com.froyo.ridekaro.viewModel.AfterClickingRideNow
import kotlinx.android.synthetic.main.fragment_my_rides.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyRidesFragment : Fragment(R.layout.fragment_my_rides) {

    private lateinit var afterClickingRideNow: AfterClickingRideNow

    override fun onResume() {
        super.onResume()
        afterClickingRideNow = ViewModelProviders.of(this).get(AfterClickingRideNow::class.java)

        afterClickingRideNow.getMapRider().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val riderCount = it
            CoroutineScope(Dispatchers.IO).launch {
                if (riderCount == 1) {
                    ShowDetials()
                }
            }
        })
    }

    private fun ShowDetials() {
        cardView2.visibility = View.VISIBLE
        btnCallRider.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:0123456789")
            startActivity(intent)
        }

        btnCancel.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Cancelling Ride")
                .setMessage("Are you sure you want to cancel the ride?")
                .setPositiveButton(
                    "Yes",
                    DialogInterface.OnClickListener { dialog, which -> changeText() })
                .setNegativeButton("No", null).show()
        }
    }

    private fun changeText() {
        tvRideStatus.text = "Cancelled"
        tvRideStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.Red))

        tvTotalRupeee2.text = "0"
        btnCallRider.visibility = View.INVISIBLE
        btnCancel.visibility = View.INVISIBLE
    }
}