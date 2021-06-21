package com.froyo.ridekaro.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.froyo.ridekaro.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_otpsecond.*
import kotlinx.android.synthetic.main.activity_otpvalidation.*
import java.util.*

class OTPSecondActivity : AppCompatActivity() {

    private var mVerificationId: String? = null
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpsecond)

        lateinit var mobileNumber: String

//        support_otp_val.setOnClickListener {
//            val i = Intent(this, HomeActivity::class.java)
//            startActivity(i)
//        }

        if (intent != null && intent.extras != null) {
            mobileNumber = intent.getStringExtra("mobileNumber").toString()
        }

        mAuth = FirebaseAuth.getInstance()

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$mobileNumber", // Phone number to verify
            60, // Timeout duration
            java.util.concurrent.TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks
        ) // OnVerificationStateChangedCallbacks

        verifyButton.setOnClickListener {
            if (!otpTextField.text.isNullOrEmpty()) {
                verifyVerificationCode(otpTextField.text.toString())
            }
//            startActivity(Intent(this, HomeActivity::class.java))
//            finish()
        }
    }

    private val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Log.d("TAG", "onVerificationCompleted:$phoneAuthCredential")
                signInWithPhoneAuthCredential(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(this@OTPSecondActivity, e.message, Toast.LENGTH_LONG).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(this@OTPSecondActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                mVerificationId = s
            }
        }

    private fun verifyVerificationCode(code: String) {
        //creating the credential
        val credential = mVerificationId?.let { PhoneAuthProvider.getCredential(it, code) }

        //signing the user
        if (credential != null) {
            signInWithPhoneAuthCredential(credential)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val i = Intent(this, HomeActivity::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                }
            }
    }

}
