package com.froyo.ridekaro.views

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.froyo.ridekaro.R
import com.froyo.ridekaro.helper.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*


class SecondScreenActivity : AppCompatActivity() {

    lateinit var locale: Locale

    lateinit var gso: GoogleSignInOptions
    lateinit var googleSignInClient: GoogleSignInClient
    val SIGN_IN_CODE = 10
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_screen)


        PreferenceHelper.getSharedPreferences(this)

        val lang = PreferenceHelper.getStringFromPreference("languagePreferenceString")
        val position = PreferenceHelper.getIntFromPreference("languagePreference")
        if (lang != null) {
            setLocal(lang, position)
        }

        val flag = PreferenceHelper.getBooleanFromPreference("languageBoolean")

        Handler().postDelayed(Runnable {
            if (flag) {
                val intent = Intent(this, LanguageScreenActivity::class.java)
                startActivity(intent)
            } else {
                when {
                    PreferenceHelper.getLoginBooleanFromPreference(USER_PHONE_LOGIN) -> {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    PreferenceHelper.getLoginBooleanFromPreference(KEY_LOGIN_WITH_OAUTH) -> {
                        initializeSignin()
                    }
                    else -> {
                        val intent = Intent(this, OTPValidation::class.java)
                        startActivity(intent)
                    }
                }
            }
        }, 1000)


    }

    fun setLocal(localeName: String, position: Int) {

        locale = Locale(localeName)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
    }


    private fun initializeSignin() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mAuth = FirebaseAuth.getInstance()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val intent: Intent = googleSignInClient.signInIntent
        startActivityForResult(intent, SIGN_IN_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_CODE) {
            var task: Task<GoogleSignInAccount>? = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try {
            if (task!!.isSuccessful) {
                val account = task.getResult(ApiException::class.java)
                PreferenceHelper.writeBooleanToPreference(KEY_LOGIN_WITH_OAUTH, true)
                updatePreference(account!!)
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("UserName", account.displayName)
                intent.putExtra("UserEmail", account.email)
                intent.putExtra("UserPhoto", account.photoUrl.toString())
                Toast.makeText(this, "Welcome ${account.displayName}", Toast.LENGTH_SHORT)
                    .show()
                saveUser(account)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Login Error " + task.exception?.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {

        }
    }

    private fun updatePreference(account: GoogleSignInAccount) {
        PreferenceHelper.writeBooleanToPreference(KEY_USER_LOGGED_IN, true)
        PreferenceHelper.writeStringToPreference(KEY_USER_GOOGLE_ID, account!!.id)
        PreferenceHelper.writeStringToPreference(
            KEY_DISPLAY_NAME,
            account.displayName
        )
        PreferenceHelper.writeStringToPreference(
            KEY_USER_GOOGLE_GMAIL,
            account.email
        )
    }

    private fun saveUser(account: GoogleSignInAccount) {

        val database = FirebaseDatabase.getInstance()

        val dbUsers = database.getReference("users").child(account.id!!)

        dbUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //even if user exit in database the FCM token will be different for each device
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseDatabase.getInstance().getReference("users")
                                .child(account.id!!)
                            val token = Objects.requireNonNull(task.result)
                            user.child("token").setValue(token)
                        } else {
                            Log.d("TAG", "onComplete: " + task.exception!!.message)
                        }
                    }
                    Toast.makeText(this@SecondScreenActivity, "Welcomeback", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                if (!snapshot.exists()) {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener {
                        if (it.isSuccessful) {
                            val token: String = Objects.requireNonNull<String>(it.result)
                            val username: String = ""
                            val user =
                                UserModel(
                                    account.email!!,
                                    account.displayName,
                                    username,
                                    account.photoUrl.toString(),
                                    null,
                                    token
                                )

                            dbUsers.setValue(user)
                                .addOnCompleteListener { it_inside ->
                                    if (it_inside.isSuccessful) {
                                        Toast.makeText(
                                            this@SecondScreenActivity,
                                            "token saved",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

//    private fun isLocationEnabled(context: Context): Boolean? {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            // This is a new method provided in API 28
//            val lm = context.getSystemService(LOCATION_SERVICE) as LocationManager
//            lm.isLocationEnabled
//        } else {
//            // This was deprecated in API 28
//            val mode = Settings.Secure.getInt(
//                context.contentResolver, Settings.Secure.LOCATION_MODE,
//                Settings.Secure.LOCATION_MODE_OFF
//            )
//            mode != Settings.Secure.LOCATION_MODE_OFF
//        }
//    }

}