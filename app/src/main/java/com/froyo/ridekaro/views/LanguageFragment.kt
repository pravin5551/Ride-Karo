package com.froyo.ridekaro.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
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
import kotlinx.android.synthetic.main.fragment_language.*
import java.util.*


class LanguageFragment : Fragment(R.layout.fragment_language) {

    lateinit var locale: Locale

    lateinit var gso: GoogleSignInOptions
    lateinit var googleSignInClient: GoogleSignInClient
    val SIGN_IN_CODE = 10
    lateinit var mAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { PreferenceHelper.getSharedPreferences(it) }

        val languages = resources.getStringArray(R.array.language)

        val arrayAdapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.item_layout_language,
                languages
            )
        }

        autoCompleteTextView2.setText(arrayAdapter?.getItem(0).toString(), false)
        autoCompleteTextView2.setAdapter(arrayAdapter)
        autoCompleteTextView2.setOnItemClickListener { parent, view, position, id ->
            when (languages[position]) {
                "English" -> setLocal("en", 0)
                "हिंदी (Hindi)" -> setLocal("hi", 1)
                "ಕನ್ನಡ (Kannada)" -> setLocal("kn", 2)
                "తెలుగు (Telugu)" -> setLocal("te", 3)
                "தமிழ் (Tamil)" -> setLocal("ta", 4)
            }

        }


        btnContinue.setOnClickListener {
//            val intent = Intent(context, HomeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
            // activity?.onBackPressed()
            //  activity?.recreate()
            initializeSignin()
        }


    }

    fun setLocal(localeName: String, position: Int) {

        locale = Locale(localeName)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)

        PreferenceHelper.writeIntToPreference("languagePreference", position)
        PreferenceHelper.writeStringToPreference("languagePreferenceString", localeName)
        PreferenceHelper.writeBooleanToPreference("languageBoolean", false)
    }

    private fun initializeSignin() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mAuth = FirebaseAuth.getInstance()

        googleSignInClient = activity?.let { GoogleSignIn.getClient(it.applicationContext, gso) }!!
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
                val intent = Intent(context, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra("UserName", account.displayName)
                intent.putExtra("UserEmail", account.email)
                intent.putExtra("UserPhoto", account.photoUrl.toString())
                Toast.makeText(
                    requireContext().applicationContext,
                    "Language changed successfully",
                    Toast.LENGTH_SHORT
                ).show()
                saveUser(account)
                startActivity(intent)
            } else {
                Toast.makeText(
                    requireContext().applicationContext,
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
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Welcomeback",
                        Toast.LENGTH_SHORT
                    )
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
                                            requireContext().applicationContext,
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

}