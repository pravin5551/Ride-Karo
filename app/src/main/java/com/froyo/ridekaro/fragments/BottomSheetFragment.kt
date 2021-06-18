package com.froyo.ridekaro.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.froyo.ridekaro.R
import com.froyo.ridekaro.viewModel.DistanceViewModel
import com.froyo.ridekaro.views.RiderComing
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*


class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var pendingIntent: PendingIntent

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
//            startActivity(Intent(context, RiderComing::class.java))

            val intent = Intent(context, RiderComing::class.java)

            pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            dislayNotification("Ride Booked", "The Rider is on his way to your location")
        }
    }

    private fun setAmount(distance: Float) {
        val totalAmount: Int = (distance * 10).toInt()
        tvTotalRupee.text = totalAmount.toString()
    }

    private fun dislayNotification(task: String, desc: String) {
        val notificationManager =
            requireActivity().applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "workExample",
                "workExample",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val builder =
            NotificationCompat.Builder(requireActivity().applicationContext, "workExample")
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.motorbike)
                .setContentIntent(pendingIntent)
        notificationManager.notify(1, builder.build())
    }

}
