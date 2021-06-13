package com.froyo.ridekaro.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.froyo.ridekaro.R
import kotlinx.android.synthetic.main.activity_otpvalidation.*

class OTPValidation : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpvalidation)

        button.setOnClickListener {

            if (mobileNumber.text.count() == 10) {
                val i = Intent(this, OTPSecondActivity::class.java).apply {
                    putExtra("mobileNumber", mobileNumber.text.toString())
                }
                startActivity(i)
            } else {
                Toast.makeText(this, "Enter 10 digits mobile number", Toast.LENGTH_LONG).show()
            }
        }
    }
}