package com.froyo.ridekaro.views.navDrawerFragments

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.froyo.ridekaro.NotificationService
import com.froyo.ridekaro.R
import com.froyo.ridekaro.views.HomeActivity
import kotlinx.android.synthetic.main.fragment_payment.*

class PaymentFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvPaytmLink.setOnClickListener {
            val appPackageName = "net.one97.paytm"
            val pm: PackageManager = context?.applicationContext!!.getPackageManager()

            val appstart = pm.getLaunchIntentForPackage(appPackageName)
            if (null != appstart) {
                context?.getApplicationContext()!!.startActivity(appstart)
            } else {
                Toast.makeText(
                    context?.getApplicationContext(), R.string.Install_PayTm_on_your_device,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        tvMobikwik.setOnClickListener {
            val appPackageName = "com.mobikwik_new"
            val pm: PackageManager = context?.applicationContext!!.getPackageManager()
            val appstart = pm.getLaunchIntentForPackage(appPackageName)
            if (null != appstart) {
                context?.getApplicationContext()!!.startActivity(appstart)
            } else {
                Toast.makeText(
                    context?.getApplicationContext(),
                    R.string.Install_Mobikwik_on_your_device,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        tvGooglePay.setOnClickListener {

            val appPackageName = "com.google.android.apps.walletnfcrel"
            val pm: PackageManager = context?.applicationContext!!.getPackageManager()
            val appstart = pm.getLaunchIntentForPackage(appPackageName)
            if (null != appstart) {
                context?.getApplicationContext()!!.startActivity(appstart)
            } else {
                Toast.makeText(
                    context?.getApplicationContext(), R.string.Install_GooglePay_on_your_device,
                    Toast.LENGTH_SHORT
                ).show()
            }


        }

        btnPayment.setOnClickListener {
            AlertBox()

        }
        tvPayCash.setOnClickListener {
            AlertBox()
        }


    }

    private fun AlertBox() {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.Complete_the_Transaction)
        builder.setTitle(R.string.Payment_Alert)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.yes) { dialog, which ->
            Toast.makeText(
                activity,
                R.string.Congratulations_Payment_successful_Done,
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(activity, NotificationService::class.java)
            requireActivity().startService(intent)

            val intent1 = Intent(activity, HomeActivity::class.java)
            requireActivity().startActivity(intent1)
            activity?.finish()
        }
        builder.setNegativeButton(R.string.no) { dialog, which ->
            Toast.makeText(activity, R.string.payment_failed, Toast.LENGTH_LONG).show()
            dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()


    }


}