package com.froyo.ridekaro.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.froyo.ridekaro.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_otpvalidation.*


const val RC_SIGN_IN = 123

class OTPValidation : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpvalidation)

        mAuth = FirebaseAuth.getInstance();

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()


        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogle.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent;
            startActivityForResult(signInIntent, RC_SIGN_IN);

            val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
            if (acct != null) {
//
//                val bundle = Bundle()
//                val name = acct.displayName
//                val email = acct.email
//
//                val bundle = bundleOf("name" to acct.displayName, "email" to acct.email,"photo" to acct.photoUrl.toString())
//
//                val fragment = ProfileFragment()
//                val fragmentManager: FragmentManager = supportFragmentManager
//                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//                fragment.arguments = (bundle)
//                fragmentTransaction.replace(R.id.googleContainer, fragment,"fragment").addToBackStack("fragment").commit()
                val intent_two = Intent(this, HomeActivity::class.java)
                intent_two.putExtra("UserName",acct.displayName)
                intent_two.putExtra("UserEmail",acct.email)
                intent_two.putExtra("UserPhoto",acct.photoUrl.toString())
                startActivity(intent_two)
            }

        }

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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
//        }
//    }
//
//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val acc = completedTask.getResult(ApiException::class.java)
//            Toast.makeText(this@OTPValidation, "Signed In Successfully", Toast.LENGTH_SHORT).show()
//            FirebaseGoogleAuth(acc)
//        } catch (e: ApiException) {
//            Toast.makeText(this@OTPValidation, "Sign In Failed", Toast.LENGTH_SHORT).show()
//            FirebaseGoogleAuth(null)
//        }
//    }
//
//    private fun FirebaseGoogleAuth(acct: GoogleSignInAccount?) {
//        //check if the account is null
//        if (acct != null) {
//            val authCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
//            mAuth.signInWithCredential(authCredential)
//                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
//                    override fun onComplete(task: Task<AuthResult>) {
//                        if (task.isSuccessful) {
//                            Toast.makeText(this@OTPValidation, "Successful", Toast.LENGTH_SHORT)
//                                .show()
//                            val user: FirebaseUser? = mAuth.getCurrentUser()
//                            if (user != null) {
//                                updateUI(user)
//                            }
//                        } else {
//                            Toast.makeText(this@OTPValidation, "Failed", Toast.LENGTH_SHORT).show()
//                            updateUI(null)
//                        }
//                    }
//
//                })
//        } else {
//            Toast.makeText(this@OTPValidation, "acc failed", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    private fun updateUI(fUser: FirebaseUser?) {
////        btnSignOut.setVisibility(View.VISIBLE)
//        val account = GoogleSignIn.getLastSignedInAccount(applicationContext)
//        if (account != null) {
//            val personName = account.displayName
//            val personGivenName = account.givenName
//            val personFamilyName = account.familyName
//            val personEmail = account.email
//            val personId = account.id
//            val personPhoto: Uri? = account.photoUrl
//            Toast.makeText(this@OTPValidation, "$personName $personEmail", Toast.LENGTH_SHORT)
//                .show()
//        }
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

    }
}





















